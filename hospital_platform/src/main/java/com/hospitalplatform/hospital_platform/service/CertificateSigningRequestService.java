package com.hospitalplatform.hospital_platform.service;

import com.hospitalplatform.hospital_platform.dto.CSRAutofillDataDTO;
import com.hospitalplatform.hospital_platform.dto.CertificateSigningRequestDTO;
import com.hospitalplatform.hospital_platform.exception.impl.InvalidAPIResponse;
import org.bouncycastle.operator.OperatorCreationException;

import java.io.IOException;
import java.security.Principal;

public interface CertificateSigningRequestService {
    void sendRequest(CertificateSigningRequestDTO csr) throws OperatorCreationException, IOException, InvalidAPIResponse;
    CSRAutofillDataDTO getAutofillData(Principal principal);
}
