package com.admin.platform.service.impl;

import com.admin.platform.constants.CryptConstants;
import com.admin.platform.model.CertificateSigningRequest;
import com.admin.platform.model.DigitalCertificate;
import com.admin.platform.model.IssuerData;
import com.admin.platform.model.SubjectData;
import com.admin.platform.repository.DigitalCertificateRepository;
import com.admin.platform.service.CertificateSigningRequestService;
import com.admin.platform.service.DigitalCertificateService;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class DigitalCertificateServiceImpl implements DigitalCertificateService {

    @Autowired
    private DigitalCertificateRepository digitalCertificateRepository;

    @Autowired
    private CertificateSigningRequestService certificateSigningRequestService;

    @Override
    public void createCertificate(Long csrId, String templateName) {
        CertificateSigningRequest crs = certificateSigningRequestService.findById(csrId);
        //TODO: template selection and adding extensions to certificate


    }

    @Override
    public void createCertificate(String templateName) {

    }

    @Override
    public List<DigitalCertificate> getAll() {
        return null;
    }

    @Override
    public DigitalCertificate getBySerialNumber(BigInteger serialNumber) {
        return null;
    }

    public KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(2048, random);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public IssuerData generateIssuerData(PrivateKey issuerKey, X500Name name) {
        return new IssuerData(issuerKey, name);
    }

    @Override
    public SubjectData generateSubjectData(PublicKey publicKey, X500Name name) {
        return new SubjectData(
                publicKey,
                name,
                String.valueOf(1000), //TODO: place different kind of generic value maybe?
                new Date(),
                generateDate(12)
        );
    }

    @Override
    public X509Certificate generateCertificate(SubjectData subjectData, IssuerData issuerData) {
        try {
            JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
            builder = builder.setProvider("BC");

            ContentSigner contentSigner = builder.build(issuerData.getPrivateKey());

            X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(issuerData.getX500name(),
                    new BigInteger(subjectData.getSerialNumber()),
                    subjectData.getStartDate(),
                    subjectData.getEndDate(),
                    subjectData.getX500name(),
                    subjectData.getPublicKey());

            certGen.addExtension(Extension.keyUsage, false,
                    new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyCertSign | KeyUsage.cRLSign));

            certGen.addExtension(Extension.basicConstraints, false, new BasicConstraints(true));


            byte[] subjectKeyIdentifier = new JcaX509ExtensionUtils()
                    .createSubjectKeyIdentifier(subjectData.getPublicKey()).getKeyIdentifier();

            certGen.addExtension(Extension.subjectKeyIdentifier, false,
                    new SubjectKeyIdentifier(subjectKeyIdentifier));

            X509CertificateHolder certHolder = certGen.build(contentSigner);
            JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
            certConverter = certConverter.setProvider("BC");

            return certConverter.getCertificate(certHolder);
        } catch (IllegalArgumentException | IllegalStateException | OperatorCreationException |
                CertificateException | NoSuchAlgorithmException | CertIOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private X509Certificate generateCertificate(CertificateSigningRequest crs, String templateName) {
        return null;
    }

    private X509Certificate generateCertificate(CertificateSigningRequest crs) {
        /*try {

            JcaContentSignerBuilder builder = new JcaContentSignerBuilder(CryptConstants.ENCRYPTION_ALGORITHM);

            builder = builder.setProvider(CryptConstants.BC_PROVIDER);

            ContentSigner contentSigner = builder.build(issuerData.getPrivateKey());

            X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(issuerData.getX500name(),
                    new BigInteger(crs.getId().toString()),
                    new Date(),
                    subjectData.getEndDate(),
                    subjectData.getX500name(),
                    subjectData.getPublicKey());

            // Generise se sertifikat
            X509CertificateHolder certHolder = certGen.build(contentSigner);

            // Builder generise sertifikat kao objekat klase X509CertificateHolder
            // Nakon toga je potrebno certHolder konvertovati u sertifikat, za sta se koristi certConverter
            JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
            certConverter = certConverter.setProvider("BC");

            // Konvertuje objekat u sertifikat
            return certConverter.getCertificate(certHolder);
        } catch (IllegalArgumentException | IllegalStateException | OperatorCreationException | CertificateException e) {
            e.printStackTrace();
        }*/
        return null;
    }

    private Date generateDate(int periodInMonths) {
        Calendar calendarLater = Calendar.getInstance();
        calendarLater.setTime(new Date());

        calendarLater.add(Calendar.MONTH, periodInMonths);

        return calendarLater.getTime();
    }
}
