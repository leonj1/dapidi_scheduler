package com.dapidi.scheduler.models;

import com.dapidi.scheduler.enums.JobInstanceState;
import com.dapidi.scheduler.enums.RunState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.UUID;

/**
 * This is tracking the started Thread that's running somewhere.
 *
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class JobRun implements Comparable<JobRun>{
    private static final Logger log = LoggerFactory.getLogger(JobRun.class);
    private UUID id;
    private UUID jobInstanceId;
    private RunState runState;
    private JobInstanceState jobState;
    private Integer exitCode;
    private Instant startTime;
    private Instant endTime;
    transient private JobInstance jobInstance;

    public JobRun(UUID id, UUID jobInstanceId, RunState runState, JobInstanceState jobState, Integer exitCode, Instant startTime, Instant endTime) {
        this(jobInstanceId, runState, exitCode, startTime, endTime);
        this.id = id;
        this.jobState = jobState;
    }

    public JobRun(UUID jobInstanceId, RunState runState, Integer exitCode, Instant startTime, Instant endTime) {
        this.jobInstanceId = jobInstanceId;
        this.runState = runState;
        this.exitCode = exitCode;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getJobInstanceId() {
        return jobInstanceId;
    }

    public void setJobInstanceId(UUID jobInstanceId) {
        this.jobInstanceId = jobInstanceId;
    }

    public RunState getRunState() {
        return runState;
    }

    public void setRunState(RunState runState) {
        this.runState = runState;
    }

    public JobInstanceState getJobState() {
        return jobState;
    }

    public void setJobState(JobInstanceState jobState) {
        this.jobState = jobState;
    }

    public Integer getExitCode() {
        return exitCode;
    }

    public void setExitCode(Integer exitCode) {
        this.exitCode = exitCode;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public JobInstance getJobInstance() {
        return jobInstance;
    }

    public void setJobInstance(JobInstance jobInstance) {
        this.jobInstance = jobInstance;
    }

    @Override
    public int compareTo(JobRun o) {
        // compareTo should return < 0 if this is supposed to be
        // less than other, > 0 if this is supposed to be greater than
        // other and 0 if they are supposed to be equal
        if (this.getRunState().ordinal() < o.getRunState().ordinal()) {
            return -1;
        } else if (this.getRunState().ordinal() > o.getRunState().ordinal()) {
            return 1;
        }
        return 0;
    }
}
