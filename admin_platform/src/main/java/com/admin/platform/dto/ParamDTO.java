package com.admin.platform.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ParamDTO {
    @Pattern(regexp = "[a-zA-Z0-9]+")
    private String name;
    @NotEmpty
    private String regex;

    public ParamDTO() {
    }

    public ParamDTO(String name, String regex) {
        this.name = name;
        this.regex = regex;
    }

    public String getName() {
        return name;
    }

    public String getRegex() {
        return regex;
    }
}
