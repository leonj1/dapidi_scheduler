package com.dapidi.scheduler.models;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * The user-defined Task they want to run somewhere.
 * This is what they want to run where and when.
 * <p>
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class JobDefinition {
    private UUID id;
    private String name;
    private String command;
    private String schedule;
    private String machine;
    private String runAs;
    private String userProfile;
    private boolean alarmIfFail;
    private Integer retryOnFailure;
    private String cronDate;
    private String condition;
    private String stdoutFile;
    private String stderrFile;
    private String comment;
    private Integer maxRunTime;
    private Integer maxRetry;

    public JobDefinition(UUID id, String name, String command, String schedule, String machine, String runAs, String userProfile, boolean alarmIfFail, Integer retryOnFailure, String cronDate, String condition, String stdoutFile, String stderrFile, String comment, Integer maxRunTime, Integer maxRetry) {
        this(name, command, machine, schedule, runAs, userProfile, alarmIfFail, retryOnFailure, cronDate, condition, stdoutFile, stderrFile, comment);
        this.id = id;
        this.maxRunTime = maxRunTime;
        this.maxRetry = maxRetry;
    }

    public JobDefinition(String name, String command, String machine, String schedule, String runAs, String userProfile, boolean alarmIfFail, Integer retryOnFailure, String cronDate, String condition, String stdoutFile, String stderrFile, String comment) {
        this(name, command, machine, schedule);
        this.runAs = runAs;
        this.userProfile = userProfile;
        this.alarmIfFail = alarmIfFail;
        this.retryOnFailure = retryOnFailure;
        this.cronDate = cronDate;
        this.condition = condition;
        this.stdoutFile = stdoutFile;
        this.stderrFile = stderrFile;
        this.comment = comment;
    }

    public JobDefinition(String name, String command, String machine, String schedule) {
        this.name = name;
        this.command = command;
        this.machine = machine;
        this.schedule = schedule;
    }

    public String getSchedule() {
        return schedule;
    }

    public Integer getMaxRetry() {
        return maxRetry;
    }

    public void setMaxRetry(Integer maxRetry) {
        this.maxRetry = maxRetry;
    }

    public Integer getMaxRunTime() {
        return maxRunTime;
    }

    public void setMaxRunTime(Integer maxRunTime) {
        this.maxRunTime = maxRunTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setRunAs(String runAs) {
        this.runAs = runAs;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public void setAlarmIfFail(boolean alarmIfFail) {
        this.alarmIfFail = alarmIfFail;
    }

    public void setRetryOnFailure(Integer retryOnFailure) {
        this.retryOnFailure = retryOnFailure;
    }

    public void setCronDate(String cronDate) {
        this.cronDate = cronDate;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setStdoutFile(String stdoutFile) {
        this.stdoutFile = stdoutFile;
    }

    public void setStderrFile(String stderrFile) {
        this.stderrFile = stderrFile;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCommand() {
        return command;
    }

    public String getMachine() {
        return machine;
    }

    public String getRunAs() {
        return runAs;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public boolean isAlarmIfFail() {
        return alarmIfFail;
    }

    public Integer getRetryOnFailure() {
        return retryOnFailure;
    }

    public String getCronDate() {
        return cronDate;
    }

    public String getCondition() {
        return condition;
    }

    public String getStdoutFile() {
        return stdoutFile;
    }

    public String getStderrFile() {
        return stderrFile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        JobDefinition that = (JobDefinition) o;

        return new EqualsBuilder()
                .append(name, that.name)
                .append(command, that.command)
                .append(machine, that.machine)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(command)
                .append(machine)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("command", command)
                .append("machine", machine)
                .append("runAs", runAs)
                .append("userProfile", userProfile)
                .append("alarmIfFail", alarmIfFail)
                .append("retryOnFailure", retryOnFailure)
                .append("cronDate", cronDate)
                .append("condition", condition)
                .append("stdoutFile", stdoutFile)
                .append("stderrFile", stderrFile)
                .toString();
    }
}
