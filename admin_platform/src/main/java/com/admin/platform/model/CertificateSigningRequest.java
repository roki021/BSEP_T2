package com.admin.platform.model;

import javax.persistence.*;

@Entity
public class CertificateSigningRequest {
    @TableGenerator(name = "CSR_GEN", table = "ID_GEN", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "csr_gen", initialValue = 1, allocationSize = 100)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "CSR_GEN")
    private Long id;

    @Column(unique = true)
    private String commonName;

    @Column
    private String surname;

    @Column
    private String givenName;

    @Column
    private String organization;

    @Column
    private String organizationUnit;

    @Column
    private String country;

    @Column
    private String email;

    @Column
    private String uniqueIdentifier;

    @Column
    private boolean active;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] fullCertificate;

    public CertificateSigningRequest(String commonName, String surname, String givenName, String organization, String organizationUnit, String country, String email, String uniqueIdentifier, byte[] fullCertificate) {
        this.commonName = commonName;
        this.surname = surname;
        this.givenName = givenName;
        this.organization = organization;
        this.organizationUnit = organizationUnit;
        this.country = country;
        this.email = email;
        this.uniqueIdentifier = uniqueIdentifier;
        this.fullCertificate = fullCertificate;
        this.active = true; //TODO: false
    }

    public CertificateSigningRequest() {
    }

    public Long getId() {
        return id;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public byte[] getFullCertificate() {
        return fullCertificate;
    }

    public void setFullCertificate(byte[] fullCertificate) {
        this.fullCertificate = fullCertificate;
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

    public boolean isActive() { return this.active; }

    public void setActive(boolean active) { this.active = active; }

    public void activate() { this.active = true; }
}
