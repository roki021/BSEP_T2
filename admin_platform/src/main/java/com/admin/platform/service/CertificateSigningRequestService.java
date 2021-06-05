package com.admin.platform.service;


import com.admin.platform.dto.SecretCommunicationTokenDTO;
import com.admin.platform.exception.impl.UnexpectedSituation;
import com.admin.platform.model.CertificateSigningRequest;
import org.bouncycastle.asn1.x509.GeneralNames;

import java.io.IOException;
import java.security.PublicKey;
import java.sql.SQLException;
import java.util.List;

public interface CertificateSigningRequestService {

    SecretCommunicationTokenDTO saveRequest(byte[] request) throws IOException, UnexpectedSituation;

    void logicRemove(Long id) throws SQLException;

    List<CertificateSigningRequest> getAll();

    CertificateSigningRequest findById(Long id);

    PublicKey getPublicKeyFromCSR(Long id);

    GeneralNames getGeneralNamesFromCSR(Long id);

    void confirmCertificateSigningRequest(Long id);
}
