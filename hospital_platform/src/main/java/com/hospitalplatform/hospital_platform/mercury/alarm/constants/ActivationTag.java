package com.hospitalplatform.hospital_platform.mercury.alarm.constants;

public enum ActivationTag {
    DEVICE(0b1),
    SEC(0b10),
    LOG_SIMULATOR(0b100);

    private int tag;

    ActivationTag(int tag) {
        this.tag = tag;
    }

    public int getTag() {
        return tag;
    }
}
