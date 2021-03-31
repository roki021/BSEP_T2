package com.hospitalplatform.hospital_platform.exception;

public abstract class JSONException extends Exception {
    protected int errorCode;
    protected String message;

    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
