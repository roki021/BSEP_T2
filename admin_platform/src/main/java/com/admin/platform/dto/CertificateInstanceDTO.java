package com.admin.platform.dto;

import com.admin.platform.exception.impl.UnexpectedSituation;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;

import java.util.Date;
import java.util.List;

public class CertificateInstanceDTO {

    private String commonName;
    private String organizationName;
    private String organizationUnit;
    private String country;
    private String email;
    private Date startFrom;
    private Date endTo;
    private Long serialNumber;
    private List<String> keyUsage;
    private String revokeReason;

    public CertificateInstanceDTO(X500Name x500Name) {
        try {
            this.commonName = getX509NameField(x500Name, BCStyle.CN);
            this.organizationName = getX509NameField(x500Name, BCStyle.O);
            this.organizationUnit = getX509NameField(x500Name, BCStyle.OU);
            this.country = getX509NameField(x500Name, BCStyle.C);
            this.email = getX509NameField(x500Name, BCStyle.EmailAddress);
        } catch (UnexpectedSituation unexpectedSituation) {
            unexpectedSituation.printStackTrace();
        }
    }

    public CertificateInstanceDTO() {
    }

    public CertificateInstanceDTO(String commonName, String organizationName, String organizationUnit, String country, String email, Date startFrom, Date endTo, Long serialNumber) {
        this.commonName = commonName;
        this.organizationName = organizationName;
        this.organizationUnit = organizationUnit;
        this.country = country;
        this.email = email;
        this.startFrom = startFrom;
        this.endTo = endTo;
        this.serialNumber = serialNumber;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
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

    public Date getStartFrom() {
        return startFrom;
    }

    public void setStartFrom(Date startFrom) {
        this.startFrom = startFrom;
    }

    public Date getEndTo() {
        return endTo;
    }

    public void setEndTo(Date endTo) {
        this.endTo = endTo;
    }

    public Long getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Long serialNumber) {
        this.serialNumber = serialNumber;
    }

    public List<String> getKeyUsage() {
        return keyUsage;
    }

    public void setKeyUsage(List<String> keyUsage) {
        this.keyUsage = keyUsage;
    }

    public String getRevokeReason() {
        return revokeReason;
    }

    public void setRevokeReason(String revokeReason) {
        this.revokeReason = revokeReason;
    }

    private String getX509NameField(X500Name x500Name, ASN1ObjectIdentifier field) throws UnexpectedSituation {
        RDN[] rdn = x500Name.getRDNs(field);

        if (rdn.length == 0)
            return "";
        else if (rdn.length != 1)
            throw new UnexpectedSituation(
                    "CertificateSigningRequestService: RDN expected only one param. (given: " + rdn.length + ")");

        return IETFUtils.valueToString(rdn[0].getFirst().getValue());
    }
}
