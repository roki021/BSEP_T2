package com.hospitalplatform.hospital_platform.service.imple;

import com.hospitalplatform.hospital_platform.service.CertificateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Service
public class CertificateServiceImpl implements CertificateService {
    @Value("${hospital.cert_path}")
    private String certificatePath;

    @Override
    public void installCertificate(String certificate_string) throws IOException {
        FileWriter writer = new FileWriter(certificatePath);
        writer.write(certificate_string);
        writer.close();
    }

    @Override
    public PrivateKey readPrivateKey(String keyStorePath, String keyStorePass, String alias) {
        try {
            KeyStore ks = KeyStore.getInstance("JKS", "SUN");
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStorePath));
            ks.load(in, keyStorePass.toCharArray());

            if (ks.isKeyEntry(alias)) {
                return (PrivateKey) ks.getKey(alias, keyStorePass.toCharArray());
            }
        } catch (KeyStoreException | NoSuchAlgorithmException | NoSuchProviderException | CertificateException
                | IOException | UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public X509Certificate readCertificate(String keyStorePath, String keyStorePass, String alias) {
        try {
            KeyStore ks = KeyStore.getInstance("JKS", "SUN");
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStorePath));
            ks.load(in, keyStorePass.toCharArray());

            if (ks.isKeyEntry(alias) || ks.isCertificateEntry(alias)) {
                return (X509Certificate) ks.getCertificate(alias);
            }
        } catch (KeyStoreException | NoSuchAlgorithmException | NoSuchProviderException | CertificateException
                | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
