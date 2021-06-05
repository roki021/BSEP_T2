package com.hospitalplatform.hospital_platform.dto;

import com.hospitalplatform.hospital_platform.validator.ValidCountryName;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class CertificateSigningRequestDTO {

    private Long id;

    @Pattern(regexp = "[a-z]+[a-z-.]*:\\d+")
    private String commonName;

    @NotBlank
    private String surname;

    @NotBlank
    private String givenName;

    @Pattern(regexp = "[a-zA-Z0-9 ]+")
    private String organization;

    @Pattern(regexp = "[a-zA-Z0-9 ]+")
    private String organizationUnit;

    @ValidCountryName
    private String country;

    @Email
    private String email;

    //TODO
    private String serialNumber;

    private String title;

    public CertificateSigningRequestDTO() {}

    public CertificateSigningRequestDTO(String commonName, String surname, String givenName, String organization, String organizationUnit, String country, String email, String serialNumber, String title) {
        this.commonName = commonName;
        this.surname = surname;
        this.givenName = givenName;
        this.organization = organization;
        this.organizationUnit = organizationUnit;
        this.country = country;
        this.email = email;
        this.serialNumber = serialNumber;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getOrganizationUnit() {
        return organizationUnit;
    }

    public void setOrganizationUnit(String organizationUnit) {
        this.organizationUnit = organizationUnit;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
