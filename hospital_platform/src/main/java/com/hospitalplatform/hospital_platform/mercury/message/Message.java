package com.hospitalplatform.hospital_platform.mercury.message;


import com.hospitalplatform.hospital_platform.mercury.alarm.constants.ActivationTag;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;

@Document
public class Message {

    @Field
    @NotNull
    private LinkedHashMap<String, Object> fields;

    @Id
    @Field
    private Long time;

    @Field
    @NotNull
    private ActivationTag tag;

    @Field
    @NotNull
    private String raw;

    public Message(Long time, LinkedHashMap<String, Object> fields, ActivationTag tag, String raw) {
        this.time = time;
        this.fields = fields;
        this.tag = tag;
        this.raw = raw;
    }

    public Message() {}

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

    public void setTag(String tagName) {
        this.tag = ActivationTag.valueOf(tagName);
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }
}
