package com.dapidi.scheduler.services;

import com.dapidi.scheduler.enums.JobInstanceState;
import com.dapidi.scheduler.enums.RunState;
import com.dapidi.scheduler.models.Job;
import com.dapidi.scheduler.models.JobInstance;
import com.dapidi.scheduler.repositories.JobDefinitionRepository;
import com.dapidi.scheduler.repositories.JobInstanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class CheckForNewJobs implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(CheckForNewJobs.class);

    private JobInstanceRepository jobInstanceRepository;
    private JobDefinitionRepository jobDefinitionRepository;
    private JobService jobService;
    private AgentService agentService;

    public CheckForNewJobs(JobInstanceRepository jobInstanceRepository, JobDefinitionRepository jobDefinitionRepository, JobService jobService, AgentService agentService) {
        this.jobInstanceRepository = jobInstanceRepository;
        this.jobDefinitionRepository = jobDefinitionRepository;
        this.jobService = jobService;
        this.agentService = agentService;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("CheckForNewJobs");
        List<Job> all = this.jobService.jobsToRunNow();
        log.info(String.format("There are %d jobs to schedule", all.size()));
        for (Job job : all) {
            job.setJobDefinition(this.jobDefinitionRepository.findOne(job.getJobDefinitionId()));
            try {
                log.info(String.format("Dispatching job_id to be run: %s", job.getId()));
                this.agentService.dispatch(
                        job,
                        this.jobInstanceRepository.findOne(
                                this.jobInstanceRepository.save(
                                        new JobInstance(
                                                job,
                                                JobInstanceState.ACTIVE)
                                )
                        ),
                        RunState.STARTED
                );
            } catch (Exception e) {
                log.error("Problem assigning the job to be performed");
                try {
                    this.jobInstanceRepository.deleteByJobId(job.getId());
                } catch (Exception e1) {
                    log.info(String.format("Did not delete jobInstance by jobId %s since it does not exist", job.getId()));
                }
            }
        }
    }
}
