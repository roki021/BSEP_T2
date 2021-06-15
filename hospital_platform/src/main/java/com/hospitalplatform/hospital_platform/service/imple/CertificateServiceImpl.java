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
import java.security.cert.CertificateFactory;
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
    public X509Certificate readCertificate(String certificatePath) {
        try {
            CertificateFactory fact = CertificateFactory.getInstance("X.509");
            FileInputStream is = new FileInputStream (certificatePath);
            X509Certificate cer = (X509Certificate) fact.generateCertificate(is);
            is.close();
            return cer;
        } catch (CertificateException| IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
