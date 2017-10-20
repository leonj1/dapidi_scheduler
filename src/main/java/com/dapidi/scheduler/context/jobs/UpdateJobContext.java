package com.dapidi.scheduler.context.jobs;

import com.dapidi.scheduler.context.JsonRequired;
import com.dapidi.scheduler.enums.JobState;
import com.google.gson.annotations.SerializedName;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class UpdateJobContext {
    @JsonRequired public String id;
    @JsonRequired @SerializedName("job_state")
    public JobState jobState;

}
