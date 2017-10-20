package com.dapidi.scheduler.repositories;

import com.dapidi.scheduler.models.JobRun;

import java.util.List;
import java.util.UUID;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public interface JobRunRepository {
    List<JobRun> findByJobInstanceId(UUID id);
    void save(JobRun item);
}
