package com.admin.platform.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class HospitalMemberDTO {
    private Integer id;

    private String name;

    private String username;

    private String role;

    //TODO: permissions
}
