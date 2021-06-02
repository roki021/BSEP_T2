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
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.*;
import java.security.cert.Certificate;
import java.sql.Timestamp;
import java.util.Properties;

@Configuration
public class PlatformKeyStore {

    @Autowired
    private DigitalCertificateService digitalCertificateService;

    @Value("${platform.keystore.filepath}")
    private String keyStoreFilePath;

    @Value("${platform.keystore.password}")
    private String keyStorePassword;

    @Value("${platform.truststore.filepath}")
    private String trustStoreFilePath;

    @Value("${platform.truststore.password}")
    private String trustStorePassword;

    @Value("${platform.certificates.directory}")
    private String certDirectory;

    @Value("${nginx.keystore.path}")
    private String keyStoreNginxPath;

    @Value("${nginx.truststore.path}")
    private String trustStoreNginxPath;

    @Bean(name = "setUpPKI")
    public void setUpPKI() throws Exception {
//        try {
            setUpKeyStore();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public String getKeyStoreFilePath() {
        return keyStoreFilePath;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    private void setUpKeyStore() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS", "SUN");
        File f = new File(keyStoreFilePath);
        if (f.exists()) {
            keyStore.load(new FileInputStream(f), keyStorePassword.toCharArray());
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
            keyStore.load(null, keyStorePassword.toCharArray());

            DigitalCertificate root_cert = generateRoot(keyStore);
            digitalCertificateService.writeCertificateToFile(keyStore,
                    TemplateTypes.ROOT.toString().toLowerCase(),
                    CryptConstants.ROOT_ALIAS, certDirectory);

            digitalCertificateService.save(root_cert);
            System.out.println("Usao");
            keyStore.store(new FileOutputStream(keyStoreFilePath), keyStorePassword.toCharArray());
        }
    }

    private void setUpTrustStore(X509Certificate certificate) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS", "SUN");
        File f = new File(trustStoreFilePath);
        if (!f.exists()) {
            keyStore.load(null, trustStorePassword.toCharArray());

            keyStore.setCertificateEntry(CryptConstants.ROOT_ALIAS, certificate);
            keyStore.store(new FileOutputStream(trustStoreFilePath), trustStorePassword.toCharArray());

            writePEMObj(certificate, trustStoreNginxPath + "/client.crt");
        }
    }

    private DigitalCertificate generateRoot(KeyStore keyStore) throws Exception {
        KeyPair kp = digitalCertificateService.generateKeyPair();
        writePEMObj(kp.getPrivate(), keyStoreNginxPath + "/root.key");

        X500NameBuilder builder = generateRootName();

        IssuerData issuerData =
                digitalCertificateService.generateIssuerData(kp.getPrivate(), builder.build());

        GeneralName[] subjectAltNames = new GeneralName[]{
                new GeneralName(GeneralName.dNSName, "localhost"),
                new GeneralName(GeneralName.dNSName, "host.docker.internal")
        };

        SubjectData subjectData = digitalCertificateService.generateSubjectData(kp.getPublic(), builder.build(),
                TemplateTypes.ROOT, new GeneralNames(subjectAltNames), "1");

        X509Certificate certificate = digitalCertificateService.generateCertificate
                (subjectData, issuerData, TemplateTypes.ROOT);
        writePEMObj(certificate, keyStoreNginxPath + "/root.crt");
        setUpTrustStore(certificate);

        keyStore.setKeyEntry(CryptConstants.ROOT_ALIAS, kp.getPrivate(),
                keyStorePassword.toCharArray(), new Certificate[]{certificate});

        DigitalCertificate digitalCertificate = new DigitalCertificate(
                new BigInteger(subjectData.getSerialNumber()));
        digitalCertificate.setStartDate(new java.sql.Timestamp(subjectData.getStartDate().getTime()));
        digitalCertificate.setEndDate(new java.sql.Timestamp(subjectData.getEndDate().getTime()));
        digitalCertificate.setCommonName(TemplateTypes.ROOT.toString());
        digitalCertificate.setAlias(CryptConstants.ROOT_ALIAS);

        return digitalCertificate;
    }

    private X500NameBuilder generateRootName(){
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, "localhost");
        builder.addRDN(BCStyle.O, "MZ-Srbija");
        builder.addRDN(BCStyle.OU, "Klinicki centar");
        builder.addRDN(BCStyle.L, "Novi Sad");
        builder.addRDN(BCStyle.C, "RS");
        return builder;
    }

    private void writePEMObj(Object objToWrite, String path) throws IOException {
        StringWriter stringWriter = new StringWriter();
        JcaPEMWriter pm = new JcaPEMWriter(stringWriter);
        pm.writeObject(objToWrite);
        pm.close();

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(stringWriter.toString());
        }
    }

    private X509Certificate readRootCert() {
        try {
            FileInputStream fis = new FileInputStream(certDirectory + "/root.crt");
            BufferedInputStream bis = new BufferedInputStream(fis);

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            return (X509Certificate)cf.generateCertificate(bis);
        } catch (CertificateException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
