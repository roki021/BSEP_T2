package com.hospitalplatform.hospital_platform.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import javax.validation.constraints.Pattern;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class RoleUpdateDTO {
    @Pattern(regexp = "admin|doctor")
    private String role;

    public String getRole() {
        return role;
    }
}
