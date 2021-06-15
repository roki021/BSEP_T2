package com.admin.platform.dto;

import com.admin.platform.validator.ValidPassword;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class LoginDTO {
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
