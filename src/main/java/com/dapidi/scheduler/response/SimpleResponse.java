package com.dapidi.scheduler.response;

import com.google.common.collect.Lists;
import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by jose on 8/26/16.
 */
public class SimpleResponse {
    @Expose private String status;
    @Expose private boolean hasDetails;
    @Expose private List<Object> details;

    public SimpleResponse(String status, List<Object> details) {
        this.status = status;
        this.hasDetails = true;
        this.details = details;
    }

    public SimpleResponse(String status) {
        this.status = status;
        this.hasDetails = false;
        this.details = Lists.newArrayList();
    }

    public String getStatus() {
        return status;
    }

    public boolean isHasDetails() {
        return hasDetails;
    }

    public List<Object> getDetails() {
        return details;
    }
}
