package com.hospitalplatform.hospital_platform.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.hospitalplatform.hospital_platform.privileges.Privilege;
import com.hospitalplatform.hospital_platform.validator.ValidPassword;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

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

    @Enumerated(EnumType.STRING)
    private List<Privilege> privileges;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public List<Privilege> getPrivileges() {
        return privileges;
    }
}
