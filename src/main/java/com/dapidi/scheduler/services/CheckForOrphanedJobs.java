package com.dapidi.scheduler.services;

import com.dapidi.scheduler.models.JobInstance;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class CheckForOrphanedJobs implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(CheckForNewJobs.class);

    private JobService jobService;
    private Gson gson;

    public CheckForOrphanedJobs(JobService jobService) {
        this.jobService = jobService;
        this.gson = new Gson();
    }

    @Override
    public void run() {
        Thread.currentThread().setName("OrphanedJobs");
        List<JobInstance> orphanedJobInstances = this.jobService.orphanedJobInstances();
        if (orphanedJobInstances.size() == 0) {
            return;
        }
        String json = this.gson.toJson(orphanedJobInstances);
        log.info(String.format("There are %d orphaned JobInstances. Setting to UNKNOWN JobInstanceState. Jobs:\n%s", orphanedJobInstances.size(), json));
        this.jobService.setToUnknownState(orphanedJobInstances);
    }
}
