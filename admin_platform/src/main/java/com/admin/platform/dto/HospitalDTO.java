package com.admin.platform.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import javax.persistence.Column;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class HospitalDTO {
    private Integer id;

    private String organization;

    private String organizationUnit;

    public HospitalDTO(Integer id, String organization, String organizationUnit) {
        this.id = id;
        this.organization = organization;
        this.organizationUnit = organizationUnit;
    }
}
