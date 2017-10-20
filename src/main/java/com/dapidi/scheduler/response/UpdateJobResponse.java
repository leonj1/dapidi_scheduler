package com.dapidi.scheduler.response;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class UpdateJobResponse implements Response {
    private boolean result;
    private String message;

    public UpdateJobResponse(boolean result, String message) {
        this.result = result;
        this.message = message;
    }

    public boolean isResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        UpdateJobResponse that = (UpdateJobResponse) o;

        return new EqualsBuilder()
                .append(result, that.result)
                .append(message, that.message)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(result)
                .append(message)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("result", result)
                .append("message", message)
                .toString();
    }
}
