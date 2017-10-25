package com.dapidi.scheduler.models;

import com.dapidi.scheduler.enums.JobState;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.UUID;

/**
 * A job_definition becomes a Job.
 * This is the parent object where started jobs reference (JobInstances)
 * OneToOne relationship with JobDefinition
 *
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class Job {
    private UUID id;
    private UUID jobDefinitionId;
    private JobState jobState;
    transient private JobDefinition jobDefinition;

    public Job(UUID id, UUID jobDefinitionId, JobState jobState) {
        this(jobDefinitionId, jobState);
        this.id = id;
    }

    public Job(UUID jobDefinitionId, JobState jobState) {
        this.jobDefinitionId = jobDefinitionId;
        this.jobState = jobState;
    }

    public void setJobDefinition(JobDefinition jobDefinition) {
        this.jobDefinition = jobDefinition;
    }

    public JobDefinition getJobDefinition() {
        return jobDefinition;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getJobDefinitionId() {
        return jobDefinitionId;
    }

    public void setJobDefinitionId(UUID jobDefinitionId) {
        this.jobDefinitionId = jobDefinitionId;
    }

    public JobState getJobState() {
        return jobState;
    }

    public void setJobState(JobState jobState) {
        this.jobState = jobState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Job job = (Job) o;

        return new EqualsBuilder()
                .append(id, job.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("jobDefinitionId", jobDefinitionId)
                .append("jobState", jobState)
                .toString();
    }
}
