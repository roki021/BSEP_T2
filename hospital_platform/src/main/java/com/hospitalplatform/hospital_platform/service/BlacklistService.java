package com.hospitalplatform.hospital_platform.service;

import com.hospitalplatform.hospital_platform.mercury.alarm.trigger.Trigger;

public interface BlacklistService {
    Trigger getBlacklistTrigger();
    void addIP(String address);
}
