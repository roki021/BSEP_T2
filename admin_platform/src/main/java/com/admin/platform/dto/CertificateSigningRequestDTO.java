package com.admin.platform.dto;

import com.admin.platform.model.CertificateSigningRequest;

public class CertificateSigningRequestDTO {

    private Long id;

    private String commonName;

    public CertificateSigningRequestDTO(CertificateSigningRequest csr) {
        this.id = csr.getId();
        this.commonName = csr.getCommonName();
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
}
