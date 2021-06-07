package com.admin.platform.dto;

import com.admin.platform.privileges.Privilege;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Pattern;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class RoleUpdateDTO {
    @Pattern(regexp = "admin|doctor")
    private String role;

    @Enumerated(EnumType.STRING)
    private List<Privilege> privileges;

    public String getRole() {
        return role;
    }

    public List<Privilege> getPrivileges() {
        return privileges;
    }
}
