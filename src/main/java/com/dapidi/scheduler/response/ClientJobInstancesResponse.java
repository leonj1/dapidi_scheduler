package com.dapidi.scheduler.response;

import java.util.List;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class ClientJobInstancesResponse {
    private String status;
    private Boolean hasDetails;
    private List<String> details;

    public ClientJobInstancesResponse(String status, Boolean hasDetails, List<String> details) {
        this.status = status;
        this.hasDetails = hasDetails;
        this.details = details;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getHasDetails() {
        return hasDetails;
    }

    public void setHasDetails(Boolean hasDetails) {
        this.hasDetails = hasDetails;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }
}
