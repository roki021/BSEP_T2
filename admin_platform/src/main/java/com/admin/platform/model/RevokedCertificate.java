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

    public RevokedCertificate() {
    }

    public RevokedCertificate(Long id, BigInteger serialNumber, Timestamp revokingDate) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.revokingDate = revokingDate;
    }

    public RevokedCertificate(BigInteger serialNumber, Timestamp revokingDate) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.revokingDate = revokingDate;
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
}
