package com.admin.platform.model;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Timestamp;

@Entity
public class RevokedCertificate {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private BigInteger serialNumber;

    @Column
    private Timestamp revokingDate;

    @Column
    private String revokeReason;

    public RevokedCertificate() {
    }

    public RevokedCertificate(Long id, BigInteger serialNumber, Timestamp revokingDate, String revokeReason) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.revokingDate = revokingDate;
        this.revokeReason = revokeReason;
    }

    public RevokedCertificate(BigInteger serialNumber, Timestamp revokingDate, String revokeReason) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.revokingDate = revokingDate;
        this.revokeReason = revokeReason;
    }

    public Long getId() {
        return id;
    }

    public BigInteger getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(BigInteger serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Timestamp getRevokingDate() {
        return revokingDate;
    }

    public void setRevokingDate(Timestamp revokingDate) {
        this.revokingDate = revokingDate;
    }

    public String getRevokeReason() {
        return revokeReason;
    }

    public void setRevokeReason(String revokeReason) {
        this.revokeReason = revokeReason;
    }
}
