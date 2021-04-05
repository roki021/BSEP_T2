package com.admin.platform.service.impl;

import com.admin.platform.exception.impl.UnexpectedSituation;
import com.admin.platform.model.CertificateSigningRequest;
import com.admin.platform.repository.CertificateSigningRequestRepository;
import com.admin.platform.service.CertificateSigningRequestService;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.pkcs.Attribute;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.List;

@Service
public class CertificateSigningRequestServiceImpl implements CertificateSigningRequestService {

    @Autowired
    private CertificateSigningRequestRepository certificateSigningRequestRepository;

    public void saveRequest(byte[] request) throws IOException, UnexpectedSituation {
        PKCS10CertificationRequest csr = this.extractCertificationRequest(request);

        X500Name x500Name = csr.getSubject();

        String commonName = getCRSX509NameField(x500Name, BCStyle.CN);
        String surname = getCRSX509NameField(x500Name, BCStyle.SURNAME);
        String givenName = getCRSX509NameField(x500Name, BCStyle.GIVENNAME);
        String organization = getCRSX509NameField(x500Name, BCStyle.O);
        String organizationUnit = getCRSX509NameField(x500Name, BCStyle.OU);
        String country = getCRSX509NameField(x500Name, BCStyle.C);
        String email = getCRSX509NameField(x500Name, BCStyle.E);
        String serialNumber = getCRSX509NameField(x500Name, BCStyle.SERIALNUMBER);

        certificateSigningRequestRepository.save(
                new CertificateSigningRequest(
                        commonName,
                        surname,
                        givenName,
                        organization,
                        organizationUnit,
                        country,
                        email,
                        serialNumber,
                        request));

    }

    @Override
    public List<CertificateSigningRequest> getAll() {
        return certificateSigningRequestRepository.findAll();
    }

    @Override
    public CertificateSigningRequest findById(Long id) {
        return certificateSigningRequestRepository.findById(id);
    }

    @Override
    public PublicKey getPublicKeyFromCSR(Long id) {
        try {
            PKCS10CertificationRequest csr = extractCertificationRequest(findById(id).getFullCertificate());

            JcaPKCS10CertificationRequest jcaCertRequest =
                    new JcaPKCS10CertificationRequest(csr.getEncoded()).setProvider("BC");
            return jcaCertRequest.getPublicKey();
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }

        return null;
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

    private String getCRSX509NameField(X500Name x500Name, ASN1ObjectIdentifier field) throws UnexpectedSituation {
        RDN[] rdn = x500Name.getRDNs(field);

        if (rdn.length == 0)
            return "";
        else if (rdn.length != 1)
            throw new UnexpectedSituation(
                    "CertificateSigningRequestService: RDN expected only one param. (given: " + rdn.length + ")");

        return IETFUtils.valueToString(rdn[0].getFirst().getValue());
    }
}
