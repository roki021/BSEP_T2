package com.admin.platform.service.impl;

import com.admin.platform.config.PlatfromKeyStore;
import com.admin.platform.constants.CryptConstants;
import com.admin.platform.constants.TemplateTypes;
import com.admin.platform.model.*;
import com.admin.platform.repository.DigitalCertificateRepository;
import com.admin.platform.repository.RevokedCertificateRepository;
import com.admin.platform.service.CertificateSigningRequestService;
import com.admin.platform.service.DigitalCertificateService;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
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
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DigitalCertificateServiceImpl implements DigitalCertificateService {

    @Autowired
    private DigitalCertificateRepository digitalCertificateRepository;

    @Autowired
    private CertificateSigningRequestService certificateSigningRequestService;

    @Autowired
    private RevokedCertificateRepository revokedCertificateRepository;

    @Autowired
    private PlatfromKeyStore platfromKeyStore;

    @Override
    public DigitalCertificate createCertificate(Long csrId, TemplateTypes templateType) {
        CertificateSigningRequest csr = certificateSigningRequestService.findById(csrId);

        PrivateKey issuerKey = readPrivateKey(platfromKeyStore.getKEYSTORE_FILE_PATH(),
                platfromKeyStore.getKEYSTORE_PASSWORD(), CryptConstants.ROOT_ALIAS,
                platfromKeyStore.getKEYSTORE_PASSWORD());
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, "ROOT");
        builder.addRDN(BCStyle.O, "MZ-Srbija");
        builder.addRDN(BCStyle.OU, "Klinicki centar");
        builder.addRDN(BCStyle.L, "Novi Sad");
        builder.addRDN(BCStyle.C, "RS");
        IssuerData issuerData = generateIssuerData(issuerKey, builder.build());
        X500NameBuilder subjectName = generateName(csr);
        SubjectData subjectData = generateSubjectData(
                certificateSigningRequestService.getPublicKeyFromCSR(csrId),
                subjectName.build(), TemplateTypes.LEAF_HOSPITAL, String.valueOf(csr.getId()));

        X509Certificate certificate = generateCertificate(subjectData, issuerData, TemplateTypes.LEAF_HOSPITAL);
        DigitalCertificate digitalCertificate = new DigitalCertificate(
                new BigInteger(csr.getId().toString()));
        digitalCertificate.setStartDate(new java.sql.Timestamp(subjectData.getStartDate().getTime()));
        digitalCertificate.setEndDate(new java.sql.Timestamp(subjectData.getEndDate().getTime()));
        digitalCertificate.setCommonName(csr.getCommonName());
        digitalCertificate.setAlias(csr.getId().toString());

        try {
            KeyStore keyStore = KeyStore.getInstance("JKS", "SUN");
            File f = new File(platfromKeyStore.getKEYSTORE_FILE_PATH());
            if (f.exists()){
                keyStore.load(new FileInputStream(f),
                        platfromKeyStore.getKEYSTORE_PASSWORD().toCharArray());

            }else {
                keyStore.load(null, platfromKeyStore.getKEYSTORE_PASSWORD().toCharArray());
            }

            save(digitalCertificate);

            keyStore.setKeyEntry(csr.getId().toString(), issuerKey,
                    platfromKeyStore.getKEYSTORE_PASSWORD().toCharArray(),
                    new Certificate[]{certificate});

            keyStore.store(new FileOutputStream(platfromKeyStore.getKEYSTORE_FILE_PATH()),
                    platfromKeyStore.getKEYSTORE_PASSWORD().toCharArray());

            return digitalCertificate;
        } catch (IOException | CertificateException | NoSuchAlgorithmException |
                NoSuchProviderException | KeyStoreException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public DigitalCertificate save(DigitalCertificate digitalCertificate) {
        return digitalCertificateRepository.save(digitalCertificate);
    }

    @Override
    public List<DigitalCertificate> getAll() {
        return digitalCertificateRepository.findAll();
    }

    @Override
    public DigitalCertificate getBySerialNumber(BigInteger serialNumber) {
        return digitalCertificateRepository.findBySerialNumber(serialNumber).orElse(null);
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
    public SubjectData generateSubjectData(PublicKey publicKey, X500Name name,
                                           TemplateTypes templateType, String serialNum) {
        Date endDate;

        if (templateType == TemplateTypes.ROOT) {
            endDate = generateDate(CryptConstants.ROOT_PERIOD_MONTHS);
        } else {
            endDate = generateDate(CryptConstants.LEAF_PERIOD_MONTHS);
        }

        return new SubjectData(
                publicKey,
                name,
                serialNum,
                new Date(),
                endDate
        );
    }

    @Override
    public X509Certificate generateCertificate(SubjectData subjectData,
                                               IssuerData issuerData,
                                               TemplateTypes templateTypes) {
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

            if(templateTypes == TemplateTypes.ROOT) {
                certGen.addExtension(Extension.keyUsage, false,
                        new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyCertSign | KeyUsage.cRLSign));

                certGen.addExtension(Extension.basicConstraints, false, new BasicConstraints(true));


                byte[] subjectKeyIdentifier = new JcaX509ExtensionUtils()
                        .createSubjectKeyIdentifier(subjectData.getPublicKey()).getKeyIdentifier();

                certGen.addExtension(Extension.subjectKeyIdentifier, false,
                        new SubjectKeyIdentifier(subjectKeyIdentifier));
            } else {
                certGen.addExtension(Extension.keyUsage, false,
                        new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyEncipherment));
                certGen.addExtension(Extension.basicConstraints, false, new BasicConstraints(false));
            }

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

    @Override
    public void writeCertificateToFile(KeyStore keyStore, String certName, String alias, String saveDirectory) throws Exception {

        java.security.cert.Certificate[] chain = keyStore.getCertificateChain(alias);

        StringWriter stringWriter = new StringWriter();
        JcaPEMWriter pm = new JcaPEMWriter(stringWriter);
        for(Certificate certificate : chain) {
            X509Certificate a = (X509Certificate)certificate;
            pm.writeObject(a);
        }
        pm.close();


        String fileName = certName + ".crt";
        String path = saveDirectory + "/" + fileName;

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(stringWriter.toString());
        }
    }

    @Override
    public void revokeCertificate(Long serialNumber) throws Exception {
        Optional<DigitalCertificate> cert = digitalCertificateRepository.
                findBySerialNumber(new BigInteger(serialNumber.toString()));
        if (cert.isEmpty()) {
            throw new Exception("Certificate with this serial number does not exist in this context");
        }

        revokedCertificateRepository.save(new RevokedCertificate(
                cert.get().getSerialNumber(),
                new Timestamp(new Date().getTime())
        ));
    }

    @Override
    public boolean isRevoked(Long serialNumber) {
        return revokedCertificateRepository.
                findBySerialNumber(new BigInteger(serialNumber.toString())).isPresent();
    }


    private Date generateDate(int periodInMonths) {
        Calendar calendarLater = Calendar.getInstance();
        calendarLater.setTime(new Date());

        calendarLater.add(Calendar.MONTH, periodInMonths);

        return calendarLater.getTime();
    }

    public PrivateKey readPrivateKey(String keyStoreFile, String keyStorePass, String alias, String pass) {
        try {
            KeyStore ks = KeyStore.getInstance("JKS", "SUN");
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
            ks.load(in, keyStorePass.toCharArray());

            if (ks.isKeyEntry(alias)) {
                PrivateKey pk = (PrivateKey) ks.getKey(alias, pass.toCharArray());
                return pk;
            }
        } catch (KeyStoreException | NoSuchAlgorithmException | NoSuchProviderException | CertificateException
                | IOException | UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    private X500NameBuilder generateName(CertificateSigningRequest csr) {
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, csr.getCommonName());
        builder.addRDN(BCStyle.O, csr.getOrganization());
        builder.addRDN(BCStyle.OU, csr.getOrganizationUnit());
        builder.addRDN(BCStyle.C, csr.getCountry());
        builder.addRDN(BCStyle.EmailAddress, csr.getEmail());
        return builder;
    }
}
