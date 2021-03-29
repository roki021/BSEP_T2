package com.admin.platform.service.impl;

import com.admin.platform.model.CertificateSigningRequest;
import com.admin.platform.repository.CertificateSigningRequestRepository;
import com.admin.platform.service.CertificateSigningRequestService;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.pkcs.Attribute;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.List;

@Service
public class CertificateSigningRequestServiceImpl implements CertificateSigningRequestService {

    @Autowired
    private CertificateSigningRequestRepository certificateSigningRequestRepository;

    public void saveRequest(byte[] request) throws IOException {
        PKCS10CertificationRequest csr = this.extractCertificationRequest(request);

        X500Name x500Name = csr.getSubject();
        String common = IETFUtils.valueToString(x500Name.getRDNs(BCStyle.CN)[0].getFirst().getValue());

        certificateSigningRequestRepository.save(new CertificateSigningRequest(common, request));
    }

    @Override
    public List<CertificateSigningRequest> getAll() {
        return certificateSigningRequestRepository.findAll();
    }

    private PKCS10CertificationRequest extractCertificationRequest(byte[] rawRequest) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(rawRequest);
        Reader pemReader = new BufferedReader(new InputStreamReader(bis));
        PEMParser pemParser = new PEMParser(pemReader);

        Object parsedObj = pemParser.readObject();
        if (parsedObj instanceof PKCS10CertificationRequest) {
            return (PKCS10CertificationRequest) parsedObj;
        }

        throw new IOException();
    }
}
