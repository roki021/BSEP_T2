package com.admin.platform.config;

import com.admin.platform.constants.CryptConstants;
import com.admin.platform.constants.TemplateTypes;
import com.admin.platform.model.DigitalCertificate;
import com.admin.platform.model.IssuerData;
import com.admin.platform.model.SubjectData;
import com.admin.platform.service.DigitalCertificateService;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.sql.Timestamp;
import java.util.Properties;

@Configuration
public class PlatfromKeyStore {

    @Autowired
    private DigitalCertificateService digitalCertificateService;

    @Value("${platform.keystore.filepath}")
    private String KEYSTORE_FILE_PATH;

    @Value("${platform.keystore.password}")
    private String KEYSTORE_PASSWORD;

    @Value("${platform.certifacates.directory}")
    private String certDirectory;

    @Bean(name = "setUpPKI")
    public KeyStore setUpPKI() {
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS", "SUN");
            File f = new File(KEYSTORE_FILE_PATH);
            if (f.exists()){
                keyStore.load(new FileInputStream(f), KEYSTORE_PASSWORD.toCharArray());
                X509Certificate cert = readRootCert();
                if(cert != null) {
                    X500Name x500name = new JcaX509CertificateHolder(cert).getSubject();
                    RDN cn = x500name.getRDNs(BCStyle.CN)[0];
                    digitalCertificateService.save(new DigitalCertificate(
                            cert.getSerialNumber(),
                            new Timestamp(cert.getNotBefore().getTime()),
                            new Timestamp(cert.getNotAfter().getTime()),
                            IETFUtils.valueToString(cn.getFirst().getValue()),
                            cert.getSerialNumber().toString()
                    ));
                }
            } else {
                keyStore.load(null, KEYSTORE_PASSWORD.toCharArray());

                DigitalCertificate root_cert = generateRoot(keyStore);
                digitalCertificateService.writeCertificateToFile(keyStore,
                       TemplateTypes.ROOT.toString().toLowerCase(),
                       CryptConstants.ROOT_ALIAS, certDirectory);

                digitalCertificateService.save(root_cert);

                keyStore.store(new FileOutputStream(KEYSTORE_FILE_PATH), KEYSTORE_PASSWORD.toCharArray());
            }
            return keyStore;
        } catch (IOException | CertificateException | NoSuchAlgorithmException |
               NoSuchProviderException | KeyStoreException e) {
            e.printStackTrace();
        }   catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getKEYSTORE_FILE_PATH() {
        return KEYSTORE_FILE_PATH;
    }

    public String getKEYSTORE_PASSWORD() {
        return KEYSTORE_PASSWORD;
    }

    public String getCertDirectory() {
        return certDirectory;
    }

    private DigitalCertificate generateRoot(KeyStore keyStore) throws KeyStoreException {
        KeyPair kp = digitalCertificateService.generateKeyPair();

        X500NameBuilder builder = generateName("ROOT");

        IssuerData issuerData =
                digitalCertificateService.generateIssuerData(kp.getPrivate(), builder.build());
        SubjectData subjectData = digitalCertificateService.generateSubjectData
                (kp.getPublic(), builder.build(), TemplateTypes.ROOT, CryptConstants.ROOT_ALIAS);

        subjectData.setSerialNumber(CryptConstants.ROOT_ALIAS);
        X509Certificate certificate = digitalCertificateService.generateCertificate
                (subjectData, issuerData, TemplateTypes.ROOT);

        keyStore.setKeyEntry(CryptConstants.ROOT_ALIAS, kp.getPrivate(),
                KEYSTORE_PASSWORD.toCharArray(), new Certificate[]{certificate});

        DigitalCertificate digitalCertificate = new DigitalCertificate(
                new BigInteger(subjectData.getSerialNumber()));
        digitalCertificate.setStartDate(new java.sql.Timestamp(subjectData.getStartDate().getTime()));
        digitalCertificate.setEndDate(new java.sql.Timestamp(subjectData.getEndDate().getTime()));
        digitalCertificate.setCommonName(TemplateTypes.ROOT.toString());
        digitalCertificate.setAlias(certDirectory + "/root.crt");

        return digitalCertificate;
    }

    private X500NameBuilder generateName(String CN){
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, CN);
        builder.addRDN(BCStyle.O, "MZ-Srbija");
        builder.addRDN(BCStyle.OU, "Klinicki centar");
        builder.addRDN(BCStyle.L, "Novi Sad");
        builder.addRDN(BCStyle.C, "RS");
        return builder;
    }

    private X509Certificate readRootCert() {
        try {
            FileInputStream fis = new FileInputStream(certDirectory + "/root.crt");
            BufferedInputStream bis = new BufferedInputStream(fis);

            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            // Reads certificate between
            // -----BEGIN CERTIFICATE-----,
            // and
            // -----END CERTIFICATE-----.
            return (X509Certificate)cf.generateCertificate(bis);
        } catch (CertificateException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("localhost");
        mailSender.setPort(1025);

        //mailSender.setUsername("my.gmail@gmail.com");
        //mailSender.setPassword("password");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        //props.put("mail.smtp.auth", "true");
        //props.put("mail.smtp.starttls.enable", "true");
        //props.put("mail.debug", "true");

        return mailSender;
    }
}
