package com.dapidi.scheduler.enums;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 *
 * ACTIVE = this job instance has at least one job run that was started and is running
 * INACTIVE = no job runs are currently running
 * FAILED =  indicates problem scheduling this job with a machine
 **/
public enum JobInstanceState {
    ACTIVE,
    INACTIVE,
    UNKNOWN,
    FAILED,
    COMPLETED
}
