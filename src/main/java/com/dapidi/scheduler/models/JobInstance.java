package com.dapidi.scheduler.models;

import com.dapidi.scheduler.enums.JobInstanceState;
import com.dapidi.scheduler.enums.RunState;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * A JobInstance gets created based on Jobs.
 * OneToMany relationship with Job - many JobInstances per Job
 *
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class JobInstance {
    private static final Logger log = LoggerFactory.getLogger(JobInstance.class);

    private UUID id;
    private UUID jobId;
    private JobInstanceState jobState;
    private Instant createTime;
    private Map<JobInstanceState, JobRun> byInstanceState;
    private Map<RunState, JobRun> byRunState;
    private Map<UUID, JobRun> byId;
    private List<JobRun> jobRuns;
    transient private Job job;

    public JobInstance(UUID id, UUID jobId, JobInstanceState jobState, Instant createTime) {
        this(jobId, jobState);
        this.id = id;
        this.createTime = createTime;
    }

    public JobInstance(UUID jobId, JobInstanceState jobState) {
        this.jobId = jobId;
        this.jobRuns = Lists.newArrayList();
        this.createTime = Instant.now();
        this.byInstanceState = Maps.newHashMap();
        this.byRunState = Maps.newHashMap();
        this.byId = Maps.newHashMap();
        this.jobRuns = Lists.newArrayList();
        this.jobState = jobState;
    }

    public void populate(List<JobRun> jobRuns) {
        byInstanceState.clear();;
        byRunState.clear();
        byId.clear();
        this.jobRuns = jobRuns;
        Collections.sort(this.jobRuns);
        for(JobRun j : jobRuns) {
            byInstanceState.put(j.getJobState(), j);
            byRunState.put(j.getRunState(), j);
            byId.put(j.getId(), j);
        }
    }

    public RunState getCurrentState() {
        if (this.jobRuns.size() == 0) {
            return null;
        }
        // getting the first element since populate should sort the list by RunState ordinal order
        return this.jobRuns.get(0).getRunState();
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getJobId() {
        return jobId;
    }

    public void setJobId(UUID jobId) {
        this.jobId = jobId;
    }

    public JobInstanceState getJobState() {
        return jobState;
    }

    public void setJobState(JobInstanceState jobState) {
        this.jobState = jobState;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        JobInstance that = (JobInstance) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(jobId, that.jobId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(jobId)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("jobId", jobId)
                .append("jobState", jobState)
                .append("job", job)
                .toString();
    }
}
