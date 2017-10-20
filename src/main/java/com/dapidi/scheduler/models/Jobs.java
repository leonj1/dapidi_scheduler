package com.dapidi.scheduler.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class Jobs {
    private Job job;
    @SerializedName("job_instance")
    private JobInstance jobInstance;

    public Jobs(Job job, JobInstance jobInstance) {
        this.job = job;
        this.jobInstance = jobInstance;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public JobInstance getJobInstance() {
        return jobInstance;
    }

    public void setJobInstance(JobInstance jobInstance) {
        this.jobInstance = jobInstance;
    }
}
