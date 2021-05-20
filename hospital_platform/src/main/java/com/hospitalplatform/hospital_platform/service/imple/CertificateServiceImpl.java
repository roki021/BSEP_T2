package com.hospitalplatform.hospital_platform.service.imple;

import com.hospitalplatform.hospital_platform.service.CertificateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;

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
}
