package com.hospitalplatform.hospital_platform.service.imple;

import com.hospitalplatform.hospital_platform.service.CertificateService;
import com.hospitalplatform.hospital_platform.service.OCSPService;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.ocsp.OCSPObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.ocsp.CertificateID;
import org.bouncycastle.cert.ocsp.OCSPReq;
import org.bouncycastle.cert.ocsp.OCSPReqBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcDigestCalculatorProvider;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

@Service
public class OCSPServiceImpl implements OCSPService {

    @Value("${hospital.keystore.filepath}")
    private String keyStoreFile;

    @Value("${hospital.keystore.password}")
    private String keyStorePass;

    @Autowired
    private CertificateService certificateService;

    @Override
    public byte[] generateOCSPRequestEncoded(X509Certificate cert,
                                             X509Certificate issuerCert) throws Exception {
        BcDigestCalculatorProvider util = new BcDigestCalculatorProvider();

        CertificateID id = new CertificateID(util.get(CertificateID.HASH_SHA1),
                new X509CertificateHolder(issuerCert.getEncoded()), cert.getSerialNumber());

        OCSPReqBuilder ocspGen = new OCSPReqBuilder();
        ocspGen.addRequest(id);

        BigInteger nonce = BigInteger.valueOf(System.currentTimeMillis());
        Extension ext = new Extension(OCSPObjectIdentifiers.id_pkix_ocsp_nonce, true, new DEROctetString(nonce.toByteArray()));
        ocspGen.setRequestExtensions(new Extensions(new Extension[] { ext }));

        OCSPReq request = ocspGen.build();

        return request.getEncoded();
    }
}
