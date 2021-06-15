package com.admin.platform.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class LoggerDTO {
    @Min(1)
    private int readPeriod;

    @NotEmpty
    private String logPath;

    @Size(min=1)
    @Valid
    private List<ParamDTO> params;

    public LoggerDTO() {
    }

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
