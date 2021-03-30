package com.admin.platform.exception.impl;

import com.admin.platform.exception.JSONException;

public class UnexpectedSituation extends JSONException {
    public UnexpectedSituation(String message) {
        this.errorCode = 500L;
        this.message = message;
    }
}
