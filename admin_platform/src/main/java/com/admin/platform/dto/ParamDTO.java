package com.admin.platform.dto;

public class ParamDTO {
    private String name;
    private String regex;

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
