package com.hospitalplatform.hospital_platform.mercury.logger.impl;

import com.hospitalplatform.hospital_platform.mercury.alarm.constants.ActivationTag;
import com.hospitalplatform.hospital_platform.mercury.logger.Logger;
import com.hospitalplatform.hospital_platform.mercury.message.Message;

import java.util.LinkedHashMap;

public class AuthLogger extends Logger {
    public AuthLogger(LinkedHashMap<String, String> fieldsRegex) {
        super(fieldsRegex);
    }

    @Override
    public void writeMessage(String message) {
        LinkedHashMap<String, Object> params = super.logMessageParser.parse(message);
        if (params == null)
            return;
        Message msg = new Message(Long.parseLong(params.get("time").toString()), params, ActivationTag.SEC);
        super.broker.writeMessage(msg);
    }
}
