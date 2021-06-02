package com.admin.platform.dto;

import com.admin.platform.validator.ValidPassword;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class NewMemberDTO {
    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @Email
    private String email;

    @Pattern(regexp = "([a-zA-Z0-9]+){3,50}")
    private String username;

    @ValidPassword
    private String password;

    @Pattern(regexp = "(admin|doctor)")
    private String role;
}
