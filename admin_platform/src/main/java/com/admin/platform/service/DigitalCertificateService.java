package com.admin.platform.service;

import com.admin.platform.constants.TemplateTypes;
import com.admin.platform.dto.RevokeRequestDTO;
import com.admin.platform.model.DigitalCertificate;
import com.admin.platform.model.IssuerData;
import com.admin.platform.model.RevokedCertificate;
import com.admin.platform.model.SubjectData;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.GeneralNames;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.List;

public interface DigitalCertificateService {

    DigitalCertificate createCertificate(Long csrId, TemplateTypes templateType);

    DigitalCertificate save(DigitalCertificate digitalCertificate);

    List<DigitalCertificate> getAll();

    DigitalCertificate getBySerialNumber(BigInteger serialNumber);

    X509Certificate getX509BySerialNumber(BigInteger serialNumber);

    KeyPair generateKeyPair();

    IssuerData generateIssuerData(PrivateKey issuerKey, X500Name name);

    SubjectData generateSubjectData(PublicKey publicKey, X500Name name, TemplateTypes templateType, GeneralNames generalNames, String serialNum);

    X509Certificate generateCertificate(SubjectData subjectData, IssuerData issuerData, TemplateTypes templateTypes);

    void writeCertificateToFile(KeyStore keyStore, String certName, String alias, String saveDirectory) throws Exception;

    X500Name getSubjectName(Long serialNumber) throws CertificateEncodingException;

    List<String> getCertKeyUsage(Long serialNumber);

    Certificate readCertificate(String keyStoreFile, String keyStorePass, String alias);
}