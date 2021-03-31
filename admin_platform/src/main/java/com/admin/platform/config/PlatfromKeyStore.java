package com.admin.platform.config;

import com.admin.platform.constants.CryptConstants;
import com.admin.platform.model.IssuerData;
import com.admin.platform.model.SubjectData;
import com.admin.platform.service.DigitalCertificateService;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

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
           KeyStore trustStore = KeyStore.getInstance("JKS", "SUN");
           File f = new File(KEYSTORE_FILE_PATH);
           if (f.exists()){
               keyStore.load(new FileInputStream(f), KEYSTORE_PASSWORD.toCharArray());
           }else {
               keyStore.load(null, KEYSTORE_PASSWORD.toCharArray());

               X509Certificate root_cert = generateRoot(keyStore);
               this.writeCertificateToFile(keyStore, CryptConstants.ROOT_ALIAS);

               keyStore.store(new FileOutputStream(KEYSTORE_FILE_PATH), KEYSTORE_PASSWORD.toCharArray());
           }
           return keyStore;
       } catch (IOException | CertificateException | NoSuchAlgorithmException |
               NoSuchProviderException | KeyStoreException e) {
           e.printStackTrace();
       } catch (Exception e) {
           e.printStackTrace();
       }

        return null;
    }

    private X509Certificate generateRoot(KeyStore keyStore) throws KeyStoreException {
        KeyPair kp = digitalCertificateService.generateKeyPair();

        X500NameBuilder builder = generateName("ROOT");

        IssuerData issuerData =
                digitalCertificateService.generateIssuerData(kp.getPrivate(), builder.build());
        SubjectData subjectData = digitalCertificateService.generateSubjectData
                (kp.getPublic(), builder.build());

        subjectData.setSerialNumber(CryptConstants.ROOT_ALIAS);
        Certificate certificate = digitalCertificateService.generateCertificate
                (subjectData, issuerData);

        keyStore.setKeyEntry(CryptConstants.ROOT_ALIAS, kp.getPrivate(),
                KEYSTORE_PASSWORD.toCharArray(), new Certificate[]{certificate});

        return (X509Certificate) certificate;
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

    private void writeCertificateToFile(KeyStore keyStore, String alias) throws Exception {

        Certificate[] chain = keyStore.getCertificateChain(alias);

        StringWriter sw = new StringWriter();
        JcaPEMWriter pm = new JcaPEMWriter(sw);
        for(Certificate certificate : chain) {
            X509Certificate a = (X509Certificate)certificate;
            pm.writeObject(a);
        }
        pm.close();


        String fileName = "cert_" + alias + ".crt";
        String path = certDirectory + "/" + fileName;


        try(BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(sw.toString());
        }
    }
}
