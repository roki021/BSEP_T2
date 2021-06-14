package com.admin.platform.dto;

import java.util.List;

public class LoggerDTO {
    private int readPeriod;
    private String logPath;
    private List<ParamDTO> params;

    public LoggerDTO(int readPeriod, String logPath, List<ParamDTO> params) {
        this.readPeriod = readPeriod;
        this.logPath = logPath;
        this.params = params;
    }

    public int getReadPeriod() {
        return readPeriod;
    }

    public String getLogPath() {
        return logPath;
    }

    public List<ParamDTO> getParams() {
        return params;
    }
}
