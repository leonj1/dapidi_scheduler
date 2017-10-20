package com.dapidi.scheduler.services;

import com.dapidi.scheduler.models.Job;
import com.dapidi.scheduler.services.JobService;
import com.dapidi.scheduler.services.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class CheckJobsAfterSleeping {
    private static final Logger log = LoggerFactory.getLogger(Scheduler.class);

    private JobService jobService;
    private Scheduler scheduler;

    public CheckJobsAfterSleeping(JobService jobService, Scheduler scheduler) {
        this.jobService = jobService;
        this.scheduler = scheduler;
    }

    @PostConstruct
    public void reconcile() {
        // reconcile any jobs that may have been running while we were sleeping
        log.info("Fetching all jobs");
        List<Job> jobs = this.jobService.loadAllJobs();
        scheduler.check(jobs);
    }
}
