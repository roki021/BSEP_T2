package com.admin.platform.model;

import javax.persistence.*;

@Entity
public class CertificateSigningRequest {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String commonName;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] fullCertificate;

    public CertificateSigningRequest(String commonName, byte[] fullCertificate) {
        this.commonName = commonName;
        this.fullCertificate = fullCertificate;
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
}
