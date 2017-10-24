package com.dapidi.scheduler.services;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class WebRestResponse implements RestResponse {
    private int status;
    private String response;

    public WebRestResponse(int status, String response) {
        this.status = status;
        this.response = response;
    }

    @Override
    public int status() {
        return status;
    }

    @Override
    public String response() {
        return response;
    }
}
