package com.hospitalplatform.hospital_platform.service;

import java.security.cert.X509Certificate;

public interface OCSPService {
    byte[] generateOCSPRequestEncoded(X509Certificate cert, X509Certificate issuerCert) throws Exception;
}
