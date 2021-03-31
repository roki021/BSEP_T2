package com.hospitalplatform.hospital_platform.exception.impl;

import com.hospitalplatform.hospital_platform.exception.JSONException;

public class InvalidAPIResponse extends JSONException {
    public InvalidAPIResponse(String message) {
        this.errorCode = 400;
        this.message = message;
    }
}
