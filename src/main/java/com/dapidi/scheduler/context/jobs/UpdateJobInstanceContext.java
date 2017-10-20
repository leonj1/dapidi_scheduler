package com.dapidi.scheduler.context.jobs;

import com.dapidi.scheduler.context.JsonRequired;
import com.dapidi.scheduler.enums.JobInstanceState;

import java.util.List;
import java.util.UUID;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class UpdateJobInstanceContext {
    @JsonRequired public UUID jobId;
    @JsonRequired public JobInstanceState jobState;
    @JsonRequired public List<JobInstanceContext> jobRuns;
}
