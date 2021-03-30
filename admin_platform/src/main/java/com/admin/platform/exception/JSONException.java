package com.admin.platform.exception;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class JSONException extends Exception {
    protected Long errorCode;
    protected String message;

    public Long getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
