package com.hospitalplatform.hospital_platform.service;

import com.hospitalplatform.hospital_platform.dto.CertificateSigningRequestDTO;
import com.hospitalplatform.hospital_platform.exception.impl.InvalidAPIResponse;
import org.bouncycastle.operator.OperatorCreationException;

import java.io.IOException;

public interface CertificateSigningRequestService {
    void sendRequest(CertificateSigningRequestDTO csr) throws OperatorCreationException, IOException, InvalidAPIResponse;
}
