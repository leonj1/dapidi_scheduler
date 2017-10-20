package com.dapidi.scheduler.repositories;

import com.dapidi.scheduler.models.Job;

import java.util.List;
import java.util.UUID;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public interface JobRepository {
    void save(Job item);
    Job findOne(UUID id);
    List<Job> findAll();
}
