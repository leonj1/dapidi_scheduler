package com.dapidi.scheduler.models;

import com.dapidi.scheduler.enums.AgentState;
import com.dapidi.scheduler.enums.RunState;
import com.dapidi.scheduler.response.AgentCommandResponse;
import com.dapidi.scheduler.response.ClientJobInstancesResponse;
import com.dapidi.scheduler.services.HttpGet;
import com.dapidi.scheduler.services.HttpPost;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class Agent {
    private static final Logger log = LoggerFactory.getLogger(Agent.class);

    private UUID id;
    private String machine;
    private Instant startTime;
    private Instant heartBeatTime;
    private AgentState state;
    private List<JobInstance> jobInstances;
    private Gson gson;
    private String url;

    public Agent(String machine, Integer clientPort) {
        this.machine = machine;
        this.id = UUID.randomUUID();
        this.state = AgentState.ONLINE;
        this.jobInstances = Lists.newArrayList();
        this.gson = new Gson();
        this.url = String.format("http://%s:%d",
                this.machine,
                clientPort
        );
    }

    public AgentCommandResponse command(JobForAgent job, RunState runState) throws Exception {
        String url = String.format("%s/jobs", this.url);
        String payload = this.gson.toJson(job);
        HttpPost httpPost = new HttpPost(url, payload);
        String json = httpPost.contents();
        return new AgentCommandResponse(false, json);
    }

    public List<JobInstance> getActiveJobInstances() throws Exception {
        String url = String.format("%s/jobs", this.url);
        HttpGet httpGet = new HttpGet(url);
        String json = httpGet.contents();
        ClientJobInstancesResponse response = null;
        response = this.gson.fromJson(json, ClientJobInstancesResponse.class);
        if (!"".equals(response.getStatus())) {
//        this.jobInstances.addAll(response.getInstances());
        }

        return this.jobInstances;
    }

    public boolean hasJobInstance(UUID id) throws Exception {
        getActiveJobInstances();
        for(JobInstance ji : this.jobInstances) {
            if (ji.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public AgentState getState() {
        return state;
    }

    public void setState(AgentState state) {
        this.state = state;
    }

    public UUID getId() {
        return id;
    }

    public String getMachine() {
        return machine;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getHeartBeatTime() {
        return heartBeatTime;
    }

    public void setHeartBeatTime(Instant heartBeatTime) {
        this.heartBeatTime = heartBeatTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Agent agent = (Agent) o;

        return new EqualsBuilder()
                .append(machine, agent.machine)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(machine)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("machine", machine)
                .append("startTime", startTime)
                .append("heartBeatTime", heartBeatTime)
                .toString();
    }
}
