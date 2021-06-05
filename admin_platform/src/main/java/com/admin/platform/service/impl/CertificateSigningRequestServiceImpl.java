package com.admin.platform.service.impl;

import com.admin.platform.dto.SecretCommunicationTokenDTO;
import com.admin.platform.constants.CsrType;
import com.admin.platform.event.OnCSREvent;
import com.admin.platform.exception.impl.UnexpectedSituation;
import com.admin.platform.model.CertificateSigningRequest;
import com.admin.platform.repository.CertificateSigningRequestRepository;
import com.admin.platform.service.CertificateSigningRequestService;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.Attribute;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class CertificateSigningRequestServiceImpl implements CertificateSigningRequestService {
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private CertificateSigningRequestRepository certificateSigningRequestRepository;

    public SecretCommunicationTokenDTO saveRequest(byte[] request) throws IOException, UnexpectedSituation {
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
        String title = getCRSX509NameField(x500Name, BCStyle.T);

        String token = UUID.randomUUID().toString();

        CertificateSigningRequest req = certificateSigningRequestRepository.save(
                new CertificateSigningRequest(
                        commonName,
                        surname,
                        givenName,
                        organization,
                        organizationUnit,
                        country,
                        email,
                        serialNumber,
                        CsrType.valueOf(title),
                        token,
                        request));
        eventPublisher.publishEvent(new OnCSREvent(req));

        return new SecretCommunicationTokenDTO(token);
    }

    @Override
    public void logicRemove(Long id) throws SQLException {
        CertificateSigningRequest csr = certificateSigningRequestRepository.findById(id).orElse(null);
        if(csr == null)
            throw new SQLException("No CSR with given id");
        csr.setActive(false);
        certificateSigningRequestRepository.save(csr);
    }

    @Override
    public List<CertificateSigningRequest> getAll() {
        return certificateSigningRequestRepository.findByActive(true);
    }

    @Override
    public CertificateSigningRequest findById(Long id) {
        return certificateSigningRequestRepository.findById(id).get();
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

    @Override
    public GeneralNames getGeneralNamesFromCSR(Long id) {
        try {
            PKCS10CertificationRequest csr = extractCertificationRequest(findById(id).getFullCertificate());
            for(Attribute attribute : csr.getAttributes(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest)) {
                for(ASN1Encodable value : attribute.getAttributeValues()) {
                    Extensions extensions = Extensions.getInstance(value);
                    GeneralNames gns = GeneralNames.fromExtensions(extensions, Extension.subjectAlternativeName);

                    if (gns == null) {
                        throw new NoSuchElementException("There is no GeneralNames element");
                    }

                    return gns;
                }
            }

        } catch (IOException | NoSuchElementException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void confirmCertificateSigningRequest(Long id) {
        CertificateSigningRequest csr = certificateSigningRequestRepository.getOne(id);
        csr.activate();
        certificateSigningRequestRepository.save(csr);
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
