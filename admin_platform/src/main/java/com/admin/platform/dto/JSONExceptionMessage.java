package com.admin.platform.dto;

public class JSONExceptionMessage {
    private Long errorCode;
    private String message;

    public JSONExceptionMessage(Long errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public Long getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Long errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
