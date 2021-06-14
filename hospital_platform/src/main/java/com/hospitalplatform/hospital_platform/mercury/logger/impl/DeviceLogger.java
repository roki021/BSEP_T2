package com.hospitalplatform.hospital_platform.mercury.logger.impl;

import com.hospitalplatform.hospital_platform.mercury.alarm.constants.ActivationTag;
import com.hospitalplatform.hospital_platform.mercury.logger.Logger;
import com.hospitalplatform.hospital_platform.mercury.message.Message;
import com.hospitalplatform.hospital_platform.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedHashMap;

public class DeviceLogger extends Logger {
    public DeviceLogger(LinkedHashMap<String, String> fieldsRegex) {
        super(fieldsRegex);
    }

    @Override
    public void writeMessage(String message) {
        LinkedHashMap<String, Object> params = super.logMessageParser.parse(message);
        if (params == null)
            return;

        Message msg = new Message(Long.parseLong(params.get("time").toString()), params, ActivationTag.DEVICE);
        super.broker.writeMessage(msg);
    }
}
