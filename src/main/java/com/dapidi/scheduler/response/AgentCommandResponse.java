package com.dapidi.scheduler.response;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class AgentCommandResponse implements Response {
    private boolean result;
    private String message;

    public AgentCommandResponse(boolean result, String message) {
        this.result = result;
        this.message = message;
    }

    public boolean isResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }
}
