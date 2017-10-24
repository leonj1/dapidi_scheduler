package com.dapidi.scheduler.context.agents;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class AddAgentContext {
    @SerializedName("host_name")
    private String machine;

    public String getSelf() {
        return self;
    }

    private String self;

    public AddAgentContext(String machine, String self) {
        this.machine = machine;
        this.self = self;
    }

    public String getMachine() {
        return machine;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("machine", machine)
                .append("self", self)
                .toString();
    }
}
