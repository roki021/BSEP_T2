package com.admin.platform.service;


import com.admin.platform.model.CertificateSigningRequest;

import java.io.IOException;
import java.util.List;

public interface CertificateSigningRequestService {

    void saveRequest(byte[] request) throws IOException;

    List<CertificateSigningRequest> getAll();

}
