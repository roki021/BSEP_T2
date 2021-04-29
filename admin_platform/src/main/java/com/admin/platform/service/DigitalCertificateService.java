package com.admin.platform.service;

import com.admin.platform.constants.TemplateTypes;
import com.admin.platform.model.DigitalCertificate;
import com.admin.platform.model.IssuerData;
import com.admin.platform.model.SubjectData;
import org.bouncycastle.asn1.x500.X500Name;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.List;

public interface DigitalCertificateService {

    DigitalCertificate createCertificate(Long csrId, TemplateTypes templateType);

    DigitalCertificate save(DigitalCertificate digitalCertificate);

    List<DigitalCertificate> getAll();

    DigitalCertificate getBySerialNumber(BigInteger serialNumber);

    KeyPair generateKeyPair();

    IssuerData generateIssuerData(PrivateKey issuerKey, X500Name name);

    SubjectData generateSubjectData(PublicKey publicKey, X500Name name, TemplateTypes templateType, String serialNum);

    X509Certificate generateCertificate(SubjectData subjectData, IssuerData issuerData, TemplateTypes templateTypes);

    void writeCertificateToFile(KeyStore keyStore, String certName, String alias, String saveDirectory) throws Exception;

    void revokeCertificate(Long serialNumber) throws Exception;

    boolean isRevoked(Long serialNumber);

    X500Name getSubjectName(Long serialNumber) throws CertificateEncodingException;
}
