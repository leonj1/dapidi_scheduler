package com.dapidi.scheduler.context.jobs;

import com.dapidi.scheduler.context.JsonRequired;

import java.util.UUID;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class DeleteJobContext {
    @JsonRequired
    public UUID jobId;
}
