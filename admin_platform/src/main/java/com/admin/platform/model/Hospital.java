package com.admin.platform.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Hospital {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique = true)
    private String endpoint;

    @Column
    private String organization;

    @Column
    private String organizationUnit;

    public Hospital() {}

    public Hospital(String endpoint, String organization, String organizationUnit) {
        this.endpoint = endpoint;
        this.organization = organization;
        this.organizationUnit = organizationUnit;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
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
}
