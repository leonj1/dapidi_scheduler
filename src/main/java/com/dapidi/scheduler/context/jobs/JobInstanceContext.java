package com.dapidi.scheduler.context.jobs;

import com.dapidi.scheduler.context.JsonRequired;
import com.dapidi.scheduler.enums.JobInstanceState;
import com.dapidi.scheduler.enums.RunState;
import com.google.gson.annotations.SerializedName;

import java.time.Instant;
import java.util.UUID;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class JobInstanceContext {
    @JsonRequired public UUID id;
    @JsonRequired public RunState runState;
    public Instant startTime;
    public Instant endTime;
    public Integer exitCode;
}
