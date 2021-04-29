package com.hospitalplatform.hospital_platform.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CSRAutofillDataDTO {
    private String surname;
    private String givenName;
    private String email;
    private String organization;
    private String organizationUnit;
    private String country;

    public CSRAutofillDataDTO(String surname, String givenName, String email, String organization, String organizationUnit, String country) {
        this.surname = surname;
        this.givenName = givenName;
        this.email = email;
        this.organization = organization;
        this.organizationUnit = organizationUnit;
        this.country = country;
    }
}
