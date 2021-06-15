package com.admin.platform.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class LoggersDTO {
    @Size(min=1)
    @Valid
    private List<LoggerDTO> loggers;

    public LoggersDTO() {
    }

    public LoggersDTO(List<LoggerDTO> loggers) {
        this.loggers = loggers;
    }

    public List<LoggerDTO> getLoggers() {
        return loggers;
    }
}
