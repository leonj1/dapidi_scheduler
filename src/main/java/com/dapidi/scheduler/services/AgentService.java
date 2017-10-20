package com.dapidi.scheduler.services;

import com.dapidi.scheduler.configs.AppProperties;
import com.dapidi.scheduler.enums.AgentState;
import com.dapidi.scheduler.enums.RunState;
import com.dapidi.scheduler.models.Agent;
import com.dapidi.scheduler.models.Job;
import com.dapidi.scheduler.models.JobForAgent;
import com.dapidi.scheduler.models.JobInstance;
import com.dapidi.scheduler.response.AgentCommandResponse;
import com.dapidi.scheduler.response.DispatchAgentResponse;
import com.google.common.collect.Maps;
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
//    private static final AgentHeadquarters instance = new AgentHeadquarters();

    private Map<String, Agent> agents;
    private Boolean getPhoneMissingClients;
    private Integer getClientDefaultPort;

    public AgentService(Boolean getPhoneMissingClients, Integer getClientDefaultPort) {
        this.getPhoneMissingClients = getPhoneMissingClients;
        this.getClientDefaultPort = getClientDefaultPort;
        this.agents = Maps.newHashMap();
    }

    public void checkIn(String machine) {
        if (this.agents.containsKey(machine)) {
            Agent agent = this.agents.get(machine);
            agent.setState(AgentState.ONLINE);
        } else {
            register(machine);
        }
    }

    public UUID register(String machine) {
        if (this.agents.containsKey(machine)) {
            return this.agents.get(machine).getId();
        }
        log.info(String.format("Registering machine %s", machine));
        Agent agent = new Agent(machine, this.getClientDefaultPort);
        this.agents.put(machine, agent);
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

        if (!this.agents.containsKey(machine)) {
            boolean foundHost = false;
            if (this.getPhoneMissingClients) {
                String clientUrl = String.format("http://%s:%d/health", machine, this.getClientDefaultPort);
                HttpGet httpGet = new HttpGet(clientUrl);
                try {
                    int statusCode = httpGet.statusCode();
                    if (statusCode == 200) {
                        foundHost = true;
                        register(machine);
                    } else {
                        log.error(String.format("Healthcheck failed for machine %s. Status code %d", machine, statusCode));
                    }
                } catch (Exception e) {
                    log.error(String.format("Unable to get healthcheck for machine %s. Error: %s", machine, e.getMessage()));
                }
            }
            if (!foundHost) {
                log.warn(String.format("Machine %s not registered to run job %s", machine, job.getId()));
                return new DispatchAgentResponse(false, "No agent registered to that machine.");
            }
        }

        Agent agent = this.agents.get(machine);

        // if we think the job should be running ask the target machine if they are already working on it
        boolean isBeingWorkedOn = false;
        try {
            isBeingWorkedOn = agent.hasJobInstance(jobInstance.getId());
            if (isBeingWorkedOn) {
                log.info(String.format("JobInstance %s is already being worked on", jobInstance.getId()));
                return new DispatchAgentResponse(true, "Already being worked on");
            } else {
                log.info(String.format("Telling the agent to start working on job %s", job.getId()));
                JobForAgent jobForAgent = new JobForAgent(jobInstance.getId(), job.getJobDefinition());
                AgentCommandResponse response = agent.command(jobForAgent, runState);
                log.info(String.format("Registered that we are working on job %s", job.getId()));
                return new DispatchAgentResponse(response.isResult(), response.getMessage());
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
