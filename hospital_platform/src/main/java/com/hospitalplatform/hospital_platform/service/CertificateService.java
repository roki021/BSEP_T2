package com.hospitalplatform.hospital_platform.service;

import java.io.IOException;

public interface CertificateService {
    void installCertificate(String certificate_string) throws IOException;
}
