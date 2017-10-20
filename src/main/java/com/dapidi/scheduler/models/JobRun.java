package com.dapidi.scheduler.models;

import com.dapidi.scheduler.enums.JobInstanceState;
import com.dapidi.scheduler.enums.RunState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public JobRun(UUID jobInstanceId, RunState runState, Integer exitCode, Instant startTime, Instant endTime) {
        this.jobInstanceId = jobInstanceId;
        this.runState = runState;
        this.exitCode = exitCode;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public JobRun(UUID jobInstanceId, JobInstance jobInstance) {
        this.jobInstanceId = jobInstanceId;
        this.jobInstance = jobInstance;
    }

    public JobRun(ResultSet rs) throws SQLException {
        this.jobInstanceId = UUID.fromString(rs.getString("job_instance_id"));
//        JobInstance jobInstance = jobInstanceRepository.findOne(jobInstanceId);
//        this.jobInstance = jobInstance;
        this.runState = RunState.valueOf(rs.getString("run_state"));
        this.jobState = JobInstanceState.valueOf(rs.getString("job_instance_state"));
        this.exitCode = rs.getInt("exit_code");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        if (rs.getString("start_time") != null) {
            try {
                format.parse(rs.getString("start_time"));
                this.startTime = format.parse(rs.getString("start_time")).toInstant();
            } catch (ParseException e) {
                log.error(String.format("Problem trying to format %s", rs.getString("start_time")));
            }
        }
        if (rs.getString("end_time") != null) {
            try {
                format.parse(rs.getString("end_time"));
                this.startTime = format.parse(rs.getString("end_time")).toInstant();
            } catch (ParseException e) {
                log.error(String.format("Problem trying to format %s", rs.getString("end_time")));
            }
        }
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
