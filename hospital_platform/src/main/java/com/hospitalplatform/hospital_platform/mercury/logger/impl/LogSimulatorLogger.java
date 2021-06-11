package com.hospitalplatform.hospital_platform.mercury.logger.impl;

import com.hospitalplatform.hospital_platform.mercury.alarm.constants.ActivationTag;
import com.hospitalplatform.hospital_platform.mercury.logger.Logger;
import com.hospitalplatform.hospital_platform.mercury.message.Message;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedHashMap;

public class LogSimulatorLogger extends Logger {

    public LogSimulatorLogger(int readPeriod, String logPath, LinkedHashMap<String, String> fieldsRegex) {
        super(readPeriod, logPath, fieldsRegex);
    }

    @Override
    public void writeMessage(String message) {
        LinkedHashMap<String, Object> params = super.logMessageParser.parse(message);
        if (params == null)
            return;
        Message msg = new Message(Long.parseLong(params.get("time").toString()), params, ActivationTag.LOG_SIMULATOR);
        super.broker.writeMessage(msg);
    }

    private Long lastKnownPosition = 0L;
    private Long crunchifyCounter = 0L;

    @Override
    public void readLog() {
        RandomAccessFile readWriteFileAccess = null;
        try {
            readWriteFileAccess = new RandomAccessFile(this.logPath, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            readWriteFileAccess.seek(lastKnownPosition);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String crunchifyLine = null;
        while (true) {
            try {
                if (!((crunchifyLine = readWriteFileAccess.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println(crunchifyLine);
            //TODO
            //for (String line : crunchifyLine.split("\\n"))
            //    this.writeMessage(line);

            crunchifyCounter++;
        }
        try {
            lastKnownPosition = readWriteFileAccess.getFilePointer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            readWriteFileAccess.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
