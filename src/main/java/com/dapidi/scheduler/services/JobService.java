package com.dapidi.scheduler.services;

import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import com.dapidi.scheduler.configs.AppProperties;
import com.dapidi.scheduler.context.jobs.AddJobDefinitionContext;
import com.dapidi.scheduler.context.jobs.JobInstanceContext;
import com.dapidi.scheduler.context.jobs.UpdateJobContext;
import com.dapidi.scheduler.context.jobs.UpdateJobInstanceContext;
import com.dapidi.scheduler.enums.JobInstanceState;
import com.dapidi.scheduler.enums.JobState;
import com.dapidi.scheduler.enums.RunState;
import com.dapidi.scheduler.models.Job;
import com.dapidi.scheduler.models.JobDefinition;
import com.dapidi.scheduler.models.JobInstance;
import com.dapidi.scheduler.models.JobRun;
import com.dapidi.scheduler.models.Jobs;
import com.dapidi.scheduler.repositories.JobDefinitionRepository;
import com.dapidi.scheduler.repositories.JobInstanceRepository;
import com.dapidi.scheduler.repositories.JobRepository;
import com.dapidi.scheduler.repositories.JobRunRepository;
import com.dapidi.scheduler.response.DeleteJobResponse;
import com.dapidi.scheduler.response.Response;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static com.github.choonchernlim.betterPreconditions.preconditions.PreconditionFactory.expect;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class JobService {
    private static final Logger log = LoggerFactory.getLogger(JobService.class);
    private JobDefinitionRepository jobDefinitionRepository;
    private JobInstanceRepository jobInstanceRepository;
    private JobRepository jobRepository;
    private JobRunRepository jobRunRepository;
    private List<RunState> jobStatesThatCanBeStarted;
    private List<JobInstanceState> jobInstanceStatesThatCanBeStarted;
    private List<JobInstanceState> orphanedJobInstanceStates;
    private Integer jobOrphanedAfterDays;

    public JobService(JobDefinitionRepository jobDefinitionRepository, JobInstanceRepository jobInstanceRepository, JobRepository jobRepository, JobRunRepository jobRunRepository, List<RunState> jobStatesThatCanBeStarted, List<JobInstanceState> jobInstanceStatesThatCanBeStarted, List<JobInstanceState> orphanedJobInstanceStates, Integer jobOrphanedAfterDays) {
        this.jobDefinitionRepository = jobDefinitionRepository;
        this.jobInstanceRepository = jobInstanceRepository;
        this.jobRepository = jobRepository;
        this.jobRunRepository = jobRunRepository;
        this.jobStatesThatCanBeStarted = jobStatesThatCanBeStarted;
        this.jobInstanceStatesThatCanBeStarted = jobInstanceStatesThatCanBeStarted;
        this.orphanedJobInstanceStates = orphanedJobInstanceStates;
        this.jobOrphanedAfterDays = jobOrphanedAfterDays;
    }

    public List<RunState> getJobStatesThatCanBeStarted() {
        return jobStatesThatCanBeStarted;
    }

    public void setJobStatesThatCanBeStarted(List<RunState> jobStatesThatCanBeStarted) {
        this.jobStatesThatCanBeStarted = jobStatesThatCanBeStarted;
    }

    public List<Job> loadAllJobs() {
        return this.jobRepository.findAll();
    }

    public JobDefinition getJobByName(String name) {
        log.debug(String.format("Fetching job by name: %s", name));
        return this.jobDefinitionRepository.findByName(name);
    }

    public List<Jobs> getJobsAndInstances() {
        List<Jobs> jobs = Lists.newArrayList();
        return jobs;
    }

    public void addJob(AddJobDefinitionContext context) {
        log.info("Adding a job");
        this.jobRepository.save(
                new Job(
                        this.jobDefinitionRepository.save(
                                new JobDefinition(
                                        context.getName(),
                                        context.getCommand(),
                                        context.getMachine(),
                                        context.getSchedule(),
                                        context.getRunAs(),
                                        context.getUserProfile(),
                                        context.isAlarmIfFail(),
                                        context.getRetryOnFailure(),
                                        context.getCronDate(),
                                        context.getCondition(),
                                        context.getStdoutFile(),
                                        context.getStderrFile(),
                                        context.getComment()
                                )
                        ),
                        JobState.INACTIVE
                )
        );
        log.info("Saved Job");
    }

    public void updateJob(UUID id, UpdateJobContext context) throws Exception {
        log.info("Updating a job");
    }

    // TODO implement
    public Response deleteJob(UUID id) {
        log.info("Deleting job");
        return new DeleteJobResponse(true, "deleted");
    }

    public void updateJobInstance(UpdateJobInstanceContext context) throws Exception {
        expect(context, "context").not().toBeNull().check();
        JobInstance jobInstance = this.jobInstanceRepository.findOne(context.jobId);
        if (jobInstance == null) {
            throw new Exception(String.format("Problem since we are being asked to update a jobInstance we do not know about. %s", context.jobId));
        }

        log.info("Updating job instance");
        jobInstance.setJobState(context.jobState);
        UUID savedJobInstanceId = this.jobInstanceRepository.save(jobInstance);
        for(JobInstanceContext jobInstanceContext : context.jobRuns) {
            JobRun jobRun = new JobRun(
                    savedJobInstanceId,
                    jobInstanceContext.runState,
                    jobInstanceContext.exitCode,
                    jobInstanceContext.startTime,
                    jobInstanceContext.endTime
            );
            this.jobRunRepository.save(jobRun);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Job> jobsToRunNow() {
        // TODO This method needs lots of work.
        // Should checkForNewJobs if the job is scheduled to be run now and if there's an already running job
        List<Job> all = this.jobRepository.findAll();

        // Now filter by cron schedule
        // Source: https://github.com/jmrozanec/cron-utils
        CronDefinition cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(CronType.UNIX);
        ZonedDateTime now = ZonedDateTime.now();
        CronParser parser = new CronParser(cronDefinition);

        List<Job> jobsThatNeedToRunNowBasedOnTime = Lists.newArrayList();
        for(Job j : all) {
            JobDefinition jd = this.jobDefinitionRepository.findOne(j.getJobDefinitionId());
            j.setJobDefinition(jd);
            ExecutionTime executionTime = ExecutionTime.forCron(parser.parse(j.getJobDefinition().getSchedule()));
            // Get date for next execution
            ZonedDateTime nextExecution = executionTime.nextExecution(now);
            // Time to next execution
            Duration timeToNextExecution = executionTime.timeToNextExecution(now);
            log.debug(String.format("Date for next exec is %s and time for next exec is %s", nextExecution, timeToNextExecution));
            if (timeToNextExecution.getSeconds() < 60) {
                log.info(String.format("Job %s is candidate to be run now since its within 60 second for next run", j.getId()));
                jobsThatNeedToRunNowBasedOnTime.add(j);
            }
        }

        List<Job> reduced = Lists.newArrayList();
        for(Job j : jobsThatNeedToRunNowBasedOnTime) {
            // this checkForNewJobs is needed since RowMapper checking for empty result set could return a null object inside the List
            if (j != null && j.getId() != null) {
                List<JobInstance> found = this.jobInstanceRepository.findJobIdNotInRunState(j.getId(), jobInstanceStatesThatCanBeStarted);
                if (found.size() == 0) {
                    reduced.add(j);
                } else {
                    log.info(String.format("Not running this job since its not in a state that can be started: %s, State: %s", j.getId(), j.getJobState().name()));
                }
            }
        }
        return reduced;
    }

    public List<JobInstance> orphanedJobInstances() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime future = now.plusDays(this.jobOrphanedAfterDays);
        List<JobInstance> orphans = this.jobInstanceRepository.findNotInRunState(future, this.orphanedJobInstanceStates);
        return orphans;
    }

    public void setToUnknownState(List<JobInstance> jobInstances) {
        expect(jobInstances).not().toBeNull().check();
        if (jobInstances.size() == 0) {
            return;
        }
        JobInstanceState target = JobInstanceState.UNKNOWN;
        log.info(String.format("Setting %d jobInstances to %s state", jobInstances.size(), target.name()));
        this.jobInstanceRepository.setToJobInstanceState(jobInstances, target);
    }
}
