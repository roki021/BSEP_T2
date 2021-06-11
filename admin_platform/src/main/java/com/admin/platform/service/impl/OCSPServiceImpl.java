package com.admin.platform.service.impl;

import com.admin.platform.config.PlatformKeyStore;
import com.admin.platform.constants.CryptConstants;
import com.admin.platform.dto.RevokeRequestDTO;
import com.admin.platform.model.DigitalCertificate;
import com.admin.platform.model.RevokedCertificate;
import com.admin.platform.repository.DigitalCertificateRepository;
import com.admin.platform.repository.RevokedCertificateRepository;
import com.admin.platform.service.DigitalCertificateService;
import com.admin.platform.service.OCSPService;
import javassist.NotFoundException;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.asn1.ocsp.OCSPObjectIdentifiers;
import org.bouncycastle.asn1.x509.CRLReason;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.ocsp.*;
import org.bouncycastle.operator.bc.BcDigestCalculatorProvider;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

@Service
public class OCSPServiceImpl implements OCSPService {

    @Autowired
    private DigitalCertificateRepository digitalCertificateRepository;

    @Autowired
    private DigitalCertificateService digitalCertificateService;

    @Autowired
    private RevokedCertificateRepository revokedCertificateRepository;

    @Autowired
    private PlatformKeyStore platformKeyStore;

    @Override
    public byte[] getEncodedOCSPResponse(byte[] request) throws Exception {
        OCSPReq ocspReq = new OCSPReq(request);
        X509Certificate rootCrt = (X509Certificate)readRootCertificate();
        PrivateKey rootPrivateKey = readRootPrivateKey();

        BcDigestCalculatorProvider util = new BcDigestCalculatorProvider();

        BasicOCSPRespBuilder respBuilder = new BasicOCSPRespBuilder(
                SubjectPublicKeyInfo.getInstance(rootCrt.getPublicKey().getEncoded()),
                util.get(CertificateID.HASH_SHA1));

        Extensions extensions = null;
        Extension nonce_ext = ocspReq.getExtension(OCSPObjectIdentifiers.id_pkix_ocsp_nonce);
        if (nonce_ext != null) {
            extensions = new Extensions(new Extension[]{ nonce_ext});
        }
        respBuilder.setResponseExtensions(extensions);

        Req[] requests = ocspReq.getRequestList();
        for (Req req : requests) {
            BigInteger serialNumber = req.getCertID().getSerialNumber();
            X509Certificate cert =
                    digitalCertificateService.getX509BySerialNumber(serialNumber);

            if (cert == null) {
                respBuilder.addResponse(req.getCertID(), new UnknownStatus());
            } else {
                try {
                    cert.checkValidity();
                    boolean revoked = getIfIsRevoked(serialNumber.longValue()) != null;
                    if (revoked) {
                        respBuilder.addResponse(req.getCertID(), new RevokedStatus(new Date(), CRLReason.superseded));
                    }
                    else{
                        respBuilder.addResponse(req.getCertID(), CertificateStatus.GOOD);
                    }
                }
                catch (Exception e) {
                    respBuilder.addResponse(req.getCertID(), new RevokedStatus(new Date(), CRLReason.superseded));
                }
            }
        }

        BasicOCSPResp resp = respBuilder.build(
                new JcaContentSignerBuilder("SHA256withRSA").build(rootPrivateKey),
                null, new Date());

        OCSPRespBuilder builder = new OCSPRespBuilder();
        return builder.build(OCSPRespBuilder.SUCCESSFUL, resp).getEncoded();
    }

    @Override
    public void revokeCertificate(RevokeRequestDTO request) throws Exception {
        Optional<DigitalCertificate> cert = digitalCertificateRepository.
                findBySerialNumber(new BigInteger(request.getCertId().toString()));
        if (cert.isEmpty()) {
            throw new Exception("Certificate with this serial number does not exist in this context");
        }

        revokedCertificateRepository.save(new RevokedCertificate(
                cert.get().getSerialNumber(),
                new Timestamp(new Date().getTime()),
                request.getReason()
        ));
    }

    @Override
    public RevokedCertificate getIfIsRevoked(Long serialNumber) {
        return revokedCertificateRepository.
                findBySerialNumber(new BigInteger(serialNumber.toString())).orElse(null);
    }

    public Certificate readRootCertificate() throws Exception {
        KeyStore ks = KeyStore.getInstance("JKS", "SUN");
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(
                platformKeyStore.getKeyStoreFilePath()));
        ks.load(in, platformKeyStore.getKeyStorePassword().toCharArray());

        if (ks.isKeyEntry(CryptConstants.ROOT_ALIAS)) {
            return ks.getCertificateChain(CryptConstants.ROOT_ALIAS)[0];
        } else {
            throw new NotFoundException("Root certificate can not be found");
        }
    }

    public PrivateKey readRootPrivateKey() throws Exception {
        KeyStore ks = KeyStore.getInstance("JKS", "SUN");
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(
                platformKeyStore.getKeyStoreFilePath()));
        ks.load(in, platformKeyStore.getKeyStorePassword().toCharArray());

        if (ks.isKeyEntry(CryptConstants.ROOT_ALIAS)) {
            return (PrivateKey) ks.getKey(CryptConstants.ROOT_ALIAS,
                    platformKeyStore.getKeyStorePassword().toCharArray());
        } else {
            throw new NotFoundException("Root private key can not be found");
        }
    }
}
