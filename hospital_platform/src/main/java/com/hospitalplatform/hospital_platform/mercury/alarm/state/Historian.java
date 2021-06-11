package com.hospitalplatform.hospital_platform.mercury.alarm.state;

import java.util.HashMap;

public class Historian {
    private Long wait;
    private HashMap<String, Counter> history;

    public Historian(Long wait) {
        this.wait = wait;
        this.history = new HashMap<>();
    }

    public void update(String key, Long time) {
        Counter counter = history.getOrDefault(key, new Counter(this.wait));
        counter.update(time);
        history.put(key, counter);
    }

    public int getCount(Object key) {
        return history.get(key).getCounter();
    }
}
