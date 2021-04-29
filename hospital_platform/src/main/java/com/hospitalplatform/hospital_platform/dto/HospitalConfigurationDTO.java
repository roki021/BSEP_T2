package com.hospitalplatform.hospital_platform.dto;

public class HospitalConfigurationDTO {
    private String organization;
    private String organizationUnit;
    private String country;

    public HospitalConfigurationDTO(String organization, String organizationUnit, String country) {
        this.organization = organization;
        this.organizationUnit = organizationUnit;
        this.country = country;
    }

    public String getOrganization() {
        return organization;
    }

    private void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getOrganizationUnit() {
        return organizationUnit;
    }

    private void setOrganizationUnit(String organizationUnit) {
        this.organizationUnit = organizationUnit;
    }

    public String getCountry() {
        return country;
    }

    private void setCountry(String country) {
        this.country = country;
    }
}
