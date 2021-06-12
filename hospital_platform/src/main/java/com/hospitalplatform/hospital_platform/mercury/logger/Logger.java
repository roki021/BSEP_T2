package com.hospitalplatform.hospital_platform.mercury.logger;

import com.hospitalplatform.hospital_platform.mercury.logger.parser.LogMessageParser;
import com.hospitalplatform.hospital_platform.mercury.message.MessageBroker;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashMap;
import java.util.LinkedHashMap;

public abstract class Logger implements Runnable {
    private int activationTags;
    protected String logPath;
    private int readPeriod;
    private int ticks;
    protected MessageBroker broker;
    protected LogMessageParser logMessageParser;

    public Logger(LinkedHashMap<String, String> fieldsRegex) {
        this(0, 0, "", fieldsRegex);
    }

    public Logger(int readPeriod, String logPath, LinkedHashMap<String, String> fieldsRegex) {
        this(0, readPeriod, logPath, fieldsRegex);
    }

    public Logger(int activationTags, int readPeriod, String logPath, LinkedHashMap<String, String> fieldsRegex) {
        this.activationTags = activationTags;
        this.logPath = logPath;
        this.ticks = 0;
        this.readPeriod = readPeriod;
        this.logMessageParser = new LogMessageParser(fieldsRegex);
    }

    @Override
    public void run() {
        readLog();
    }

    public void subscribeBroker(MessageBroker broker) {
        this.broker = broker;
    }

    public abstract void writeMessage(String message);

    protected void readLog() {
    }

    public int getReadPeriod() {
        return readPeriod;
    }
}
