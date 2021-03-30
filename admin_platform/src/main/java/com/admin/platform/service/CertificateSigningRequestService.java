package com.admin.platform.service;


import com.admin.platform.exception.impl.UnexpectedSituation;
import com.admin.platform.model.CertificateSigningRequest;

import java.io.IOException;
import java.util.List;

public interface CertificateSigningRequestService {

    void saveRequest(byte[] request) throws IOException, UnexpectedSituation;

    List<CertificateSigningRequest> getAll();

}
