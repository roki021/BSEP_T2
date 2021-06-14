package com.hospitalplatform.hospital_platform.dto;

import java.util.Map;

public class AlarmDTO {

    private Long id;
    private String name;
    private int activationTags;
    private int activationThreshold;
    private String messageTracker;
    private Map<String, TriggerDTO> triggers;
    private String alarmMessage;
    private Long factWait;

    public AlarmDTO() {}

    public AlarmDTO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getActivationTags() {
        return activationTags;
    }

    public void setActivationTags(int activationTags) {
        this.activationTags = activationTags;
    }

    public int getActivationThreshold() {
        return activationThreshold;
    }

    public void setActivationThreshold(int activationThreshold) {
        this.activationThreshold = activationThreshold;
    }

    public String getMessageTracker() {
        return messageTracker;
    }

    public void setMessageTracker(String messageTracker) {
        this.messageTracker = messageTracker;
    }

    public Map<String, TriggerDTO> getTriggers() {
        return triggers;
    }

    public void setTriggers(Map<String, TriggerDTO> triggers) {
        this.triggers = triggers;
    }

    public String getAlarmMessage() {
        return alarmMessage;
    }

    public void setAlarmMessage(String alarmMessage) {
        this.alarmMessage = alarmMessage;
    }

    public Long getFactWait() {
        return factWait;
    }

    public void setFactWait(Long factWait) {
        this.factWait = factWait;
    }
}
