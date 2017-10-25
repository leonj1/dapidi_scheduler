package com.dapidi.scheduler;

import com.dapidi.scheduler.configs.AppProperties;
import com.dapidi.scheduler.configs.HikariConfig;
import com.dapidi.scheduler.controllers.CommunicationBetweenAgentsController;
import com.dapidi.scheduler.controllers.HealthCheckController;
import com.dapidi.scheduler.controllers.JobsController;
import com.dapidi.scheduler.controllers.routes.agents.AddAgentRoute;
import com.dapidi.scheduler.controllers.routes.agents.AgentCheckInRoute;
import com.dapidi.scheduler.controllers.routes.agents.ListAllAgentsRoute;
import com.dapidi.scheduler.controllers.routes.health.HealthCheckRoute;
import com.dapidi.scheduler.controllers.routes.jobs.AddJobRoute;
import com.dapidi.scheduler.controllers.routes.jobs.GetAllJobsRoute;
import com.dapidi.scheduler.controllers.routes.jobs.GetJobByNameRoute;
import com.dapidi.scheduler.controllers.routes.jobs.UpdateJobInstanceRoute;
import com.dapidi.scheduler.controllers.routes.jobs.UpdateJobRoute;
import com.dapidi.scheduler.converters.ResultSetToJob;
import com.dapidi.scheduler.converters.ResultSetToJobDefinition;
import com.dapidi.scheduler.converters.ResultSetToJobInstance;
import com.dapidi.scheduler.converters.ResultSetToJobRun;
import com.dapidi.scheduler.mappers.MyMapperResultSetExtractor;
import com.dapidi.scheduler.mappers.MyMapperRowMapper;
import com.dapidi.scheduler.repositories.JobDefinitionRepositoryImpl;
import com.dapidi.scheduler.repositories.JobInstanceRepositoryImpl;
import com.dapidi.scheduler.repositories.JobRepositoryImpl;
import com.dapidi.scheduler.repositories.JobRunRepositoryImpl;
import com.dapidi.scheduler.services.AgentService;
import com.dapidi.scheduler.services.CheckForNewJobs;
import com.dapidi.scheduler.services.CheckForOrphanedJobs;
import com.dapidi.scheduler.services.Controller;
import com.dapidi.scheduler.services.JobService;
import com.dapidi.scheduler.services.MaintenanceJobs;
import com.dapidi.scheduler.services.RestEndpoints;
import com.dapidi.scheduler.services.SchedulerAppHttpClient;
import com.josemleon.CommandlineParser;
import com.josemleon.GetEffectiveProperty;
import com.josemleon.GetProperty;
import com.josemleon.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import spark.Spark;

/**
 * Dapidi Scheduler
 * Intended to replace Computer Associates Autosys
 * <p>
 * Design: Yes, there are duplicate Repository and "mapper" objects, but that was
 * deemed acceptable since none of them hold state. Services may hold state, therefore just singletons.
 */
public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);
    private static final String APPLICATION_PROPERTIES = "application.properties";

    public static void main(String args[]) throws Exception {
        log.info("Some succeed because they are destined to. Most succeed because they are determined to. -- Unknown");

        // Starting here, we are going the job Spring would normally do
        AppProperties appProperties = null;
        Parser cmdlineParser = new CommandlineParser(args);
        try {
            appProperties = new AppProperties(
                    new GetEffectiveProperty(
                            new GetProperty(
                                    APPLICATION_PROPERTIES,
                                    cmdlineParser
                            ),
                            cmdlineParser
                    )
            );
        } catch (Exception e) {
            log.error(String.format("Really bad problem trying to find resource %s", APPLICATION_PROPERTIES));
            System.exit(1);
        }

        // Our REST endpoint
        Spark.port(appProperties.getSchedulerHttpServerPort());

        JdbcTemplate jdbcTemplate = new JdbcTemplate(
                new HikariConfig(
                        appProperties.dataSourceUrl(),
                        appProperties.databaseUser(),
                        appProperties.databasePassword()
                ).dataSource()
        );
        AgentService agentService = new AgentService(
                appProperties.getPhoneMissingClients(),
                new SchedulerAppHttpClient(
                        appProperties.clientListJobsUrl(),
                        appProperties.clientHealthUrl()
                )
        );

        JobInstanceRepositoryImpl jobInstanceRepository = new JobInstanceRepositoryImpl(
                jdbcTemplate,
                new MyMapperRowMapper<>(new ResultSetToJobInstance()),
                new MyMapperResultSetExtractor<>(new ResultSetToJobInstance())
        );

        JobDefinitionRepositoryImpl jobDefinitionRepository = new JobDefinitionRepositoryImpl(
                new MyMapperResultSetExtractor<>(new ResultSetToJobDefinition()),
                new MyMapperRowMapper<>(new ResultSetToJobDefinition()),
                jdbcTemplate
        );

        JobService jobService = new JobService(
                jobDefinitionRepository,
                jobInstanceRepository,
                new JobRepositoryImpl(
                        new MyMapperResultSetExtractor<>(new ResultSetToJob()),
                        new MyMapperRowMapper<>(new ResultSetToJob()),
                        jdbcTemplate
                ),
                new JobRunRepositoryImpl(
                        jdbcTemplate,
                        new MyMapperResultSetExtractor<>(new ResultSetToJobRun())
                ),
                appProperties.jobStatesThatCanBeStarted(),
                appProperties.jobInstanceStatesThatCanBeStarted(),
                appProperties.orphanedJobInstanceState(),
                appProperties.getJobOrphanedAfterDays()
        );

        // Everything above was what Spring used to do for us
        // Here is where the app begins

        // Now setting up REST endpoints
        RestEndpoints restEndpoints = new RestEndpoints(
                new Controller[]{
                        new JobsController(
                                new GetAllJobsRoute(jobService),
                                new AddJobRoute(jobService),
                                new UpdateJobRoute(jobService),
                                new UpdateJobInstanceRoute(jobService),
                                new GetJobByNameRoute(jobService)
                        ),
                        new CommunicationBetweenAgentsController(
                                new AddAgentRoute(agentService),
                                new AgentCheckInRoute(agentService),
                                new ListAllAgentsRoute(agentService)
                        ),
                        new HealthCheckController(
                                new HealthCheckRoute()
                        )
                }
        );
        restEndpoints.start();

        MaintenanceJobs maintenanceJobs = new MaintenanceJobs(
                new Runnable[]{
                        new CheckForNewJobs(
                                jobInstanceRepository,
                                jobDefinitionRepository,
                                jobService,
                                agentService
                        ),
                        new CheckForOrphanedJobs(
                                jobService
                        )
                },
                appProperties.getCheckInterval()
        );
        maintenanceJobs.start();
    }
}
