package com.dapidi.scheduler.repositories;

import com.dapidi.scheduler.enums.JobInstanceState;
import com.dapidi.scheduler.models.JobInstance;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public interface JobInstanceRepository {
    UUID save(JobInstance item);
    void delete(UUID id);
    void deleteByJobId(UUID id);
    JobInstance findOne(UUID id);
    List<JobInstance> findAll();
    List<JobInstance> findActive();
    List<JobInstance> findByJobId(UUID id);
    List<JobInstance> findLatestForJobs();
    List<JobInstance> findJobIdNotInRunState(UUID id, List<JobInstanceState> states);
    List<JobInstance> findNotInRunState(LocalDateTime date, List<JobInstanceState> states);
    void setToJobInstanceState(List<JobInstance> jobs, JobInstanceState state);
}
