package com.admin.platform.model;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Timestamp;

@Entity
public class DigitalCertificate  {

    @Id
    private BigInteger serialNumber;

    @Column
    private Timestamp startDate;

    @Column
    private Timestamp endDate;

    //TODO: do I need to save this like below or some other way
    @Column
    private String commonName;

    @Column
    private String certKeyStorePath;

    public DigitalCertificate(BigInteger serialNumber, Timestamp startDate, Timestamp endDate, String commonName, String certKeyStorePath) {
        this.serialNumber = serialNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.commonName = commonName;
        this.certKeyStorePath = certKeyStorePath;
    }

    public BigInteger getSerialNumber() {
        return serialNumber;
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

    public String getCertKeyStorePath() {
        return certKeyStorePath;
    }

    public void setCertKeyStorePath(String certKeyStorePath) {
        this.certKeyStorePath = certKeyStorePath;
    }
}
