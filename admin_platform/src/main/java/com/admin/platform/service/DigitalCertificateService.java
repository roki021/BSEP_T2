package com.admin.platform.service;

import com.admin.platform.model.DigitalCertificate;
import com.admin.platform.model.IssuerData;
import com.admin.platform.model.SubjectData;
import org.bouncycastle.asn1.x500.X500Name;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.List;

public interface DigitalCertificateService {

    void createCertificate(Long csrId, String templateName);

    void createCertificate(String templateName);

    List<DigitalCertificate> getAll();

    DigitalCertificate getBySerialNumber(BigInteger serialNumber);

    KeyPair generateKeyPair();

    IssuerData generateIssuerData(PrivateKey issuerKey, X500Name name);

    SubjectData generateSubjectData(PublicKey publicKey, X500Name name);

    X509Certificate generateCertificate(SubjectData subjectData, IssuerData issuerData);
}
