package com.hospitalplatform.hospital_platform.exception.impl;


import com.hospitalplatform.hospital_platform.exception.JSONException;

public class UnexpectedSituation extends JSONException {
    public UnexpectedSituation(String message) {
        this.errorCode = 500;
        this.message = message;
    }
}
