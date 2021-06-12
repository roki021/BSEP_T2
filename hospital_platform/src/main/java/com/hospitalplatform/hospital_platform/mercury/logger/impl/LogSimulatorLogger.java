package com.hospitalplatform.hospital_platform.mercury.logger.impl;

import com.hospitalplatform.hospital_platform.mercury.alarm.constants.ActivationTag;
import com.hospitalplatform.hospital_platform.mercury.logger.Logger;
import com.hospitalplatform.hospital_platform.mercury.message.Message;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedHashMap;

public class LogSimulatorLogger extends Logger {
    private File crunchifyFile = null;
    private long lastKnownPosition = 0L;
    private long crunchifyCounter = 0L;


    public LogSimulatorLogger(int readPeriod, String logPath, LinkedHashMap<String, String> fieldsRegex) {
        super(readPeriod, logPath, fieldsRegex);
        this.crunchifyFile = new File(logPath);
    }

    @Override
    public void writeMessage(String message) {
        LinkedHashMap<String, Object> params = super.logMessageParser.parse(message);
        if (params == null)
            return;
        Message msg = new Message(Long.parseLong(params.get("time").toString()), params, ActivationTag.LOG_SIMULATOR);
        super.broker.writeMessage(msg);
    }

    @Override
    public void readLog() {
        try {
                long fileLength = crunchifyFile.length();
                if (fileLength > lastKnownPosition) {

                    // Reading and writing file
                    RandomAccessFile readWriteFileAccess = new RandomAccessFile(crunchifyFile, "rw");
                    readWriteFileAccess.seek(lastKnownPosition);
                    String crunchifyLine = null;
                    while ((crunchifyLine = readWriteFileAccess.readLine()) != null) {
                        if (!crunchifyLine.contains("\\n"))
                            this.writeMessage(crunchifyLine.strip());
                        crunchifyCounter++;
                    }
                    lastKnownPosition = readWriteFileAccess.getFilePointer();
                    readWriteFileAccess.close();
                }
        } catch (Exception ignored) {
        }
    }
}
