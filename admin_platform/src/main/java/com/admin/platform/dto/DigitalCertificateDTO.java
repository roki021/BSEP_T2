package com.admin.platform.dto;

import com.admin.platform.model.DigitalCertificate;

import java.math.BigInteger;
import java.sql.Timestamp;

public class DigitalCertificateDTO {

    private BigInteger serialNumber;
    private Timestamp startDate;
    private Timestamp endDate;
    private String commonName;
    private boolean revoked;

    public DigitalCertificateDTO(DigitalCertificate digitalCertificate) {
        if(digitalCertificate != null) {
            this.serialNumber = digitalCertificate.getSerialNumber();
            this.startDate = digitalCertificate.getStartDate();
            this.endDate = digitalCertificate.getEndDate();
            this.commonName = digitalCertificate.getCommonName();
        }
    }

    public DigitalCertificateDTO(BigInteger serialNumber, Timestamp startDate, Timestamp endDate, String commonName, boolean revoked) {
        this.serialNumber = serialNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.commonName = commonName;
        this.revoked = revoked;
    }

    public DigitalCertificateDTO() {

    }

    public BigInteger getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(BigInteger serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }
}
