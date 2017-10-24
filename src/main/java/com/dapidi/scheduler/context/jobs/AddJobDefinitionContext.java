package com.dapidi.scheduler.context.jobs;

import com.google.gson.annotations.SerializedName;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class AddJobDefinitionContext {
    private String name;
    private String command;
    private String machine;
    private String schedule;
    @SerializedName("run_as")
    private String runAs;
    @SerializedName("user_profile")
    private String userProfile;
    @SerializedName("alarm_if_fail")
    private boolean alarmIfFail;
    @SerializedName("retry_on_failure")
    private Integer retryOnFailure;
    @SerializedName("cron_date")
    private String cronDate;
    private String condition;
    @SerializedName("std_out_file")
    private String stdoutFile;
    @SerializedName("std_err_file")
    private String stderrFile;
    private String comment;
    @SerializedName("max_run_time")
    private Integer maxRunTime;
    @SerializedName("max_retry")
    private Integer maxRetry;

    public AddJobDefinitionContext(String name, String command, String machine, String schedule, String runAs, String userProfile, boolean alarmIfFail, Integer retryOnFailure, String cronDate, String condition, String stdoutFile, String stderrFile, String comment, Integer maxRunTime, Integer maxRetry) {
        this.name = name;
        this.command = command;
        this.machine = machine;
        this.schedule = schedule;
        this.runAs = runAs;
        this.userProfile = userProfile;
        this.alarmIfFail = alarmIfFail;
        this.retryOnFailure = retryOnFailure;
        this.cronDate = cronDate;
        this.condition = condition;
        this.stdoutFile = stdoutFile;
        this.stderrFile = stderrFile;
        this.comment = comment;
        this.maxRunTime = maxRunTime;
        this.maxRetry = maxRetry;
    }

    // getters / setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getMachine() {
        return machine;
    }

    public void setMachine(String machine) {
        this.machine = machine;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getRunAs() {
        return runAs;
    }

    public void setRunAs(String runAs) {
        this.runAs = runAs;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public boolean isAlarmIfFail() {
        return alarmIfFail;
    }

    public void setAlarmIfFail(boolean alarmIfFail) {
        this.alarmIfFail = alarmIfFail;
    }

    public Integer getRetryOnFailure() {
        return retryOnFailure;
    }

    public void setRetryOnFailure(Integer retryOnFailure) {
        this.retryOnFailure = retryOnFailure;
    }

    public String getCronDate() {
        return cronDate;
    }

    public void setCronDate(String cronDate) {
        this.cronDate = cronDate;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getStdoutFile() {
        return stdoutFile;
    }

    public void setStdoutFile(String stdoutFile) {
        this.stdoutFile = stdoutFile;
    }

    public String getStderrFile() {
        return stderrFile;
    }

    public void setStderrFile(String stderrFile) {
        this.stderrFile = stderrFile;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getMaxRunTime() {
        return maxRunTime;
    }

    public void setMaxRunTime(Integer maxRunTime) {
        this.maxRunTime = maxRunTime;
    }

    public Integer getMaxRetry() {
        return maxRetry;
    }

    public void setMaxRetry(Integer maxRetry) {
        this.maxRetry = maxRetry;
    }
}
