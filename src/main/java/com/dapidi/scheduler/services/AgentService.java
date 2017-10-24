package com.dapidi.scheduler.services;

import com.dapidi.scheduler.context.agents.AddAgentContext;
import com.dapidi.scheduler.enums.AgentState;
import com.dapidi.scheduler.enums.RunState;
import com.dapidi.scheduler.models.Agent;
import com.dapidi.scheduler.models.Job;
import com.dapidi.scheduler.models.JobForAgent;
import com.dapidi.scheduler.models.JobInstance;
import com.dapidi.scheduler.response.AgentCommandResponse;
import com.dapidi.scheduler.response.DispatchAgentResponse;
import com.google.common.collect.Maps;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.github.choonchernlim.betterPreconditions.preconditions.PreconditionFactory.expect;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class AgentService {
    private static final Logger log = LoggerFactory.getLogger(AgentService.class);

    private Map<String, Agent> agents;
    private Boolean getPhoneMissingClients;
    private AppHttpClient appHttpClient;

    public AgentService(Boolean getPhoneMissingClients, AppHttpClient appHttpClient) {
        this.getPhoneMissingClients = getPhoneMissingClients;
        this.appHttpClient = appHttpClient;
        this.agents = Maps.newHashMap();
    }

    public void checkIn(AddAgentContext context) {
        if (this.agents.containsKey(context.getMachine())) {
            Agent agent = this.agents.get(context.getMachine());
            agent.setState(AgentState.ONLINE);
        } else {
            register(context);
        }
    }

    public UUID register(AddAgentContext context) {
        if (this.agents.containsKey(context.getMachine())) {
            return this.agents.get(context.getMachine()).getId();
        }
        log.info(String.format("Registering machine %s", context.getMachine()));
        Agent agent = new Agent(
                context.getMachine(),
                context.getSelf(),
                appHttpClient
        );
        this.agents.put(context.getMachine(), agent);
        return agent.getId();
    }

    // find the machine working on that job and handle the desired runState
    public DispatchAgentResponse dispatch(Job job, JobInstance jobInstance, RunState runState) {
        expect(job, "job").not().toBeNull().check();
        expect(job.getId(), "jobId").not().toBeNull().check();
        expect(job.getJobDefinition()).not().toBeNull().check();
        expect(job.getJobDefinition().getMachine()).not().toBeNull().not().toBeBlank().check();
        expect(jobInstance, "jobInstance").not().toBeNull().check();
        expect(jobInstance.getId(), "jobInstanceId").not().toBeNull().check();

        // find the machine where this job is expected to be running
        String machine = job.getJobDefinition().getMachine();

        if (!this.agents.containsKey(machine) && this.getPhoneMissingClients) {
            try {
                if (this.agents.get(machine).health() != HttpStatus.OK_200) {
                    log.warn(String.format("Machine %s not registered to run job %s", machine, job.getId()));
                    return new DispatchAgentResponse(false, "No agent registered to that machine.");
                }
            } catch (Exception e) {
                log.error(String.format("Unable to get healthcheck for machine %s. Error: %s", machine, e.getMessage()));
            }
        }

        Agent agent = this.agents.get(machine);

        // if we think the job should be running ask the target machine if they are already working on it
        try {
            if (agent.hasJobInstance(jobInstance.getId())) {
                log.info(String.format("JobInstance %s is already being worked on", jobInstance.getId()));
                return new DispatchAgentResponse(true, "Already being worked on");
            } else {
                log.info(String.format("Telling the agent to start working on job %s", job.getId()));
                AgentCommandResponse response = agent.command(
                        new JobForAgent(
                                jobInstance.getId(),
                                job.getJobDefinition()
                        ),
                        runState
                );
                log.info(String.format("Registered that we are working on job %s", job.getId()));
                return new DispatchAgentResponse(
                        response.isResult(),
                        response.getMessage()
                );
            }
        } catch (Exception e) {
            String message = String.format("Problem checking machine %s to see if its working on job_instance %s", machine, jobInstance.getId());
            log.error(message);
            return new DispatchAgentResponse(false, message);
        }
    }

    public Set<String> listRegisteredMachines() {
        return this.agents.keySet();
    }

}
