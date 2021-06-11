package com.admin.platform.service;

import com.admin.platform.dto.RevokeRequestDTO;
import com.admin.platform.model.RevokedCertificate;

import java.io.IOException;

public interface OCSPService {

    byte[] getEncodedOCSPResponse(byte[] request) throws IOException, Exception;

    void revokeCertificate(RevokeRequestDTO request) throws Exception;

    RevokedCertificate getIfIsRevoked(Long serialNumber);
}
