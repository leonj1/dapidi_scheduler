package com.dapidi.scheduler.models;

import com.dapidi.scheduler.enums.AgentState;
import com.dapidi.scheduler.enums.RunState;
import com.dapidi.scheduler.response.AgentCommandResponse;
import com.dapidi.scheduler.services.AppHttpClient;
import com.dapidi.scheduler.services.HttpPost;
import com.dapidi.scheduler.services.WebRestResponse;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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
//    private List<JobInstance> jobInstances;
    private Gson gson;
    private String url;
    private AppHttpClient appHttpClient;

    public Agent(String machine, String selfUrl, AppHttpClient appHttpClient) {
        this.machine = machine;
        this.appHttpClient = appHttpClient;
        this.id = UUID.randomUUID();
        this.state = AgentState.ONLINE;
//        this.jobInstances = Lists.newArrayList();
        this.gson = new Gson();
        this.url = selfUrl;
    }

    public AgentCommandResponse command(JobForAgent job, RunState runState) throws Exception {
        String url = String.format("%s/jobs", this.url);
        String payload = this.gson.toJson(job);
        HttpPost httpPost = new HttpPost(url, payload);
        String json = httpPost.contents();
        return new AgentCommandResponse(false, json);
    }

    public boolean hasJobInstance(UUID id) throws Exception {
        WebRestResponse webRestResponse = this.appHttpClient.getActiveJobInstance(
                this.url,
                id
        );
        if (webRestResponse.status() != HttpStatus.OK_200) {
            throw new Exception(
                    String.format(
                            "Problem fetching job instances for agent %s. Error: %s",
                            this.machine,
                            webRestResponse.response()
                    )
            );
        }
        return webRestResponse.status() == HttpStatus.OK_200;
    }

    public int health() throws IOException {
        return this.appHttpClient
                .health(this.url)
                .status();
    }

    // getters / setters

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
                .append(url, agent.url)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(machine)
                .append(url)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("machine", machine)
                .append("url", url)
                .append("startTime", startTime)
                .append("heartBeatTime", heartBeatTime)
                .toString();
    }
}
