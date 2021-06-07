package com.hospitalplatform.hospital_platform.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.hospitalplatform.hospital_platform.privileges.Privilege;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class HospitalUserDTO {
    @NotNull
    private Integer id;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @Email
    private String email;

    @Pattern(regexp = "([a-zA-Z0-9]+){3,50}")
    private String username;

    @Pattern(regexp = "(admin|doctor)")
    private String role;

    @Enumerated(EnumType.STRING)
    private List<Privilege> privileges;

    public HospitalUserDTO(
            Integer id,
            String firstName,
            String lastName,
            String email,
            String username,
            String role,
            List<Privilege> privileges) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.role = role;
        this.privileges = privileges;
    }

    public HospitalUserDTO() {
    }

    public Integer getId() {
        return id;
    }

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

    public String getRole() {
        return role;
    }

    public List<Privilege> getPrivileges() {
        return privileges;
    }
}
