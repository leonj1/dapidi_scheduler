package com.dapidi.scheduler.repositories;

import com.dapidi.scheduler.models.JobDefinition;

import java.util.List;
import java.util.UUID;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public interface JobDefinitionRepository {
    JobDefinition findOne(UUID id);
    UUID save(JobDefinition item);
    void delete(UUID id);
    List<JobDefinition> findAll();
    JobDefinition findByName(String name);
}
