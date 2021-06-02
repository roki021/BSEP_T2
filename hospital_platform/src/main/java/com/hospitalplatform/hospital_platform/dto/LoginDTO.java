package com.hospitalplatform.hospital_platform.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.hospitalplatform.hospital_platform.validator.ValidPassword;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class LoginDTO {
    @NotBlank(message = "Username is mandatory.")
    @Pattern(regexp = "([a-zA-Z0-9]+){3,50}")
    private String username;

    @ValidPassword
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
