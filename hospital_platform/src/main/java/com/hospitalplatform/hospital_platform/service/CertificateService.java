package com.hospitalplatform.hospital_platform.service;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public interface CertificateService {
    void installCertificate(String certificate_string) throws IOException;

    X509Certificate readCertificate(String certificatePath);
}
