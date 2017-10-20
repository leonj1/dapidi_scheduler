package com.dapidi.scheduler.models;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.UUID;

import static com.github.choonchernlim.betterPreconditions.preconditions.PreconditionFactory.expect;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class JobForAgent {
    private UUID jobInstanceId;
    private JobDefinition jobDefinition;

    public JobForAgent(UUID jobInstanceId, JobDefinition jobDefinition) {
        expect(jobInstanceId, "jobInstanceId").not().toBeNull().check();
        expect(jobDefinition, "jobDefinition").not().toBeNull().check();
        expect(jobDefinition.getId(), "jobDefinition.id").not().toBeNull().check();
        this.jobInstanceId = jobInstanceId;
        this.jobDefinition = jobDefinition;
    }

    public UUID getJobInstanceId() {
        return jobInstanceId;
    }

    public void setJobInstanceId(UUID jobInstanceId) {
        this.jobInstanceId = jobInstanceId;
    }

    public JobDefinition getJobDefinition() {
        return jobDefinition;
    }

    public void setJobDefinition(JobDefinition jobDefinition) {
        this.jobDefinition = jobDefinition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        JobForAgent that = (JobForAgent) o;

        return new EqualsBuilder()
                .append(jobInstanceId, that.jobInstanceId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(jobInstanceId)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("jobInstanceId", jobInstanceId)
                .append("jobDefinition", jobDefinition)
                .toString();
    }
}
