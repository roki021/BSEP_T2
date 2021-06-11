package com.hospitalplatform.hospital_platform.mercury.message;


import com.hospitalplatform.hospital_platform.mercury.alarm.constants.ActivationTag;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Message {
    private LinkedHashMap<String, Object> fields;
    private Long time;
    private ActivationTag tag;

    public Message(Long time, LinkedHashMap<String, Object> fields, ActivationTag tag) {
        this.time = time;
        this.fields = fields;
        this.tag = tag;
    }

    public LinkedHashMap<String, Object> getFields() {
        return fields;
    }

    public Object getField(String filedName) {
        if (!this.fields.containsKey(filedName))
            return null;
        return this.fields.get(filedName);
    }

    public boolean checkAccess(int access) {
        return (tag.getTag() & access) > 0;
    }

    public Long getTime() {
        return time;
    }
}
