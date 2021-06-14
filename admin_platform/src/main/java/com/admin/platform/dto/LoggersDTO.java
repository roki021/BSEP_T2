package com.admin.platform.dto;

import java.util.List;

public class LoggersDTO {
    private List<LoggerDTO> loggers;

    public LoggersDTO(List<LoggerDTO> loggers) {
        this.loggers = loggers;
    }

    public List<LoggerDTO> getLoggers() {
        return loggers;
    }
}
