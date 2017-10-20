package com.dapidi.scheduler.response;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class DeleteJobResponse implements Response {
    private boolean result;
    private String message;

    public DeleteJobResponse(boolean result, String message) {
        this.result = result;
        this.message = message;
    }

    @Override
    public boolean isResult() {
        return this.result;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
