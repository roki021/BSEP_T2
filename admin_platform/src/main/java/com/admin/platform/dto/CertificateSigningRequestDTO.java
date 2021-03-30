package com.admin.platform.dto;

import com.admin.platform.model.CertificateSigningRequest;

public class CertificateSigningRequestDTO {

    private Long id;
    private String commonName;
    private String surname;
    private String givenName;
    private String organization;
    private String organizationUnit;
    private String country;
    private String email;
    private String uniqueIdentifier;

    public CertificateSigningRequestDTO(CertificateSigningRequest csr) {
        this.id = csr.getId();
        this.commonName = csr.getCommonName();
        this.surname = csr.getSurname();
        this.givenName = csr.getGivenName();
        this.organization = csr.getOrganization();
        this.organizationUnit = csr.getOrganizationUnit();
        this.country = csr.getCountry();
        this.email = csr.getEmail();
        this.uniqueIdentifier = csr.getUniqueIdentifier();
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

    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public void setUniqueIdentifier(String uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }
}
