package com.dapidi.scheduler.services;

import com.dapidi.scheduler.enums.RunState;
import com.dapidi.scheduler.models.AllowedJobStates;
import com.dapidi.scheduler.models.Job;
import com.dapidi.scheduler.models.Jobs;
import com.dapidi.scheduler.response.UpdateJobResponse;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

import static com.github.choonchernlim.betterPreconditions.preconditions.PreconditionFactory.expect;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class Scheduler {
    private static final Logger log = LoggerFactory.getLogger(Scheduler.class);

    private List<Jobs> runningJobs;
    private AllowedJobStates allowedJobStates;
    private AgentService agentService;

    public Scheduler(AllowedJobStates allowedJobStates, AgentService agentService) {
        this.allowedJobStates = allowedJobStates;
        this.agentService = agentService;
        this.runningJobs = Lists.newArrayList();
    }

    public void check(List<Job> jobs) {
        // checked processing jobs
        log.info("checked processing jobs");
    }

    public UpdateJobResponse updateJob(UUID jobId, RunState destination) {
        expect(jobId, "jobId").not().toBeNull().check();
        expect(destination, "destination").not().toBeNull().check();
        for(Jobs runningJob : runningJobs) {
            if (jobId.equals(runningJob.getJob().getId())) {
                if (this.allowedJobStates.allowed(runningJob.getJobInstance().getCurrentState(), destination)) {
                    this.agentService.dispatch(
                            runningJob.getJob(),
                            runningJob.getJobInstance(),
                            destination
                    );
                    return new UpdateJobResponse(true, String.format("Updated to %s", destination.name()));
                } else {
                    return new UpdateJobResponse(false, String.format("New state %s not allowed from %s", destination.name(), runningJob.getJobInstance().getJobState().name()));
                }
            }
        }
        return new UpdateJobResponse(
                false,
                String.format("No running job found for %s", jobId.toString())
        );
    }
}
