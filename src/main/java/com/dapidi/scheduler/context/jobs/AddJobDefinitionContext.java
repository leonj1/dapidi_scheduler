package com.dapidi.scheduler.context.jobs;

import com.dapidi.scheduler.context.JsonRequired;
import com.google.gson.annotations.SerializedName;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class AddJobDefinitionContext {
    @JsonRequired
    public String name;
    @JsonRequired public String command;
    @JsonRequired public String machine;
    @JsonRequired public String schedule;
    @SerializedName("run_as")
    public String runAs;
    @SerializedName("user_profile")
    public String userProfile;
    @SerializedName("alarm_if_fail")
    public boolean alarmIfFail;
    @SerializedName("retry_on_failure")
    public Integer retryOnFailure;
    @SerializedName("cron_date")
    public String cronDate;
    public String condition;
    @SerializedName("std_out_file")
    public String stdoutFile;
    @SerializedName("std_err_file")
    public String stderrFile;
    public String comment;
    @SerializedName("max_run_time")
    public Integer maxRunTime;
    @SerializedName("max_retry")
    public Integer maxRetry;
}
