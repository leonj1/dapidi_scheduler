package com.dapidi.scheduler.services;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class MaintenanceJobs {
    private Runnable[] runnables;
    private Long schedulerCheckIntervalSeconds;

    public MaintenanceJobs(Runnable[] runnables, Long schedulerCheckIntervalSeconds) {
        this.runnables = runnables;
        this.schedulerCheckIntervalSeconds = schedulerCheckIntervalSeconds;
    }

    public void start() {
        for(Runnable r : runnables) {
            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            // TODO Find a way to configure the check interval per runnable
            executor.scheduleAtFixedRate(
                    r,
                    0,
                    this.schedulerCheckIntervalSeconds,
                    TimeUnit.SECONDS
            );
        }
    }
}
