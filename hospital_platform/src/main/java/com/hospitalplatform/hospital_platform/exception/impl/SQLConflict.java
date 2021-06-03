package com.hospitalplatform.hospital_platform.exception.impl;

import com.hospitalplatform.hospital_platform.exception.JSONException;

public class SQLConflict extends JSONException {
    public SQLConflict(String message) {
        this.errorCode = 409;
        this.message = message;
    }
}
