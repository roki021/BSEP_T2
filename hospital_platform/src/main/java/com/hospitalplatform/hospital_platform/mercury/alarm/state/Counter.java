package com.hospitalplatform.hospital_platform.mercury.alarm.state;

public class Counter {
    private Long lastTime;
    private int counter;
    private Long wait;
    private Long diff;

    public Counter(Long wait) {
        this.lastTime = 0L;
        this.counter = 1;
        this.wait = wait;
    }

    public void update(Long time) {
        this.diff = time - this.lastTime;
        if (time - this.lastTime <= this.wait)
            this.counter++;
        else
            this.counter = 1;

        this.lastTime = time;
    }

    public Long getDiff() {
        return this.diff;
    }

    public Long getLastTime() {
        return lastTime;
    }

    public int getCounter() {
        return counter;
    }

    public void reset() {
        this.counter = 0; //TODO: I think this is fine
    }
}
