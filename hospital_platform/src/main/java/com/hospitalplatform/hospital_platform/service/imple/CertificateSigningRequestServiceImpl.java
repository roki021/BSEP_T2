package com.hospitalplatform.hospital_platform.service.imple;

import com.hospitalplatform.hospital_platform.dto.CSRAutofillDataDTO;
import com.hospitalplatform.hospital_platform.dto.CertificateSigningRequestDTO;
import com.hospitalplatform.hospital_platform.dto.HospitalConfigurationDTO;
import com.hospitalplatform.hospital_platform.dto.SecretCommunicationTokenDTO;
import com.hospitalplatform.hospital_platform.exception.impl.InvalidAPIResponse;
import com.hospitalplatform.hospital_platform.models.HospitalUser;
import com.hospitalplatform.hospital_platform.repository.SecretCommunicationTokenRepository;
import com.hospitalplatform.hospital_platform.service.CertificateSigningRequestService;
import com.hospitalplatform.hospital_platform.service.HospitalService;
import com.hospitalplatform.hospital_platform.service.HospitalUserService;
import com.hospitalplatform.hospital_platform.service.SecurityService;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.security.auth.x500.X500Principal;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.security.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CertificateSigningRequestServiceImpl implements CertificateSigningRequestService {
    @Autowired
    private HospitalUserService hospitalUserService;

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    @Qualifier("ignoreSSLConfig")
    private RestTemplate ignoreSSLRestTemplate;

    @Value("${admin_platform.csr_request_url}")
    private String requestUrl;

    @Value("${hospital.key_path}")
    private String keyPath;

    @Override
    public void sendRequest(CertificateSigningRequestDTO csrDTO) throws OperatorCreationException, IOException, InvalidAPIResponse {
        KeyPair pair = generateKeyPair();

        HashMap<String, String> subjectData = new HashMap<>();

        subjectData.put("CN", csrDTO.getCommonName());
        subjectData.put("SURNAME", csrDTO.getSurname());
        subjectData.put("GIVENNAME", csrDTO.getGivenName());
        subjectData.put("O", csrDTO.getOrganization());
        subjectData.put("OU", csrDTO.getOrganizationUnit());
        subjectData.put("C", csrDTO.getCountry());
        subjectData.put("T", csrDTO.getTitle());
        subjectData.put("emailAddress", csrDTO.getEmail());
        subjectData.put("serialNumber", csrDTO.getSerialNumber());

        String mapAsString = subjectData.keySet().stream()
                .filter(key -> subjectData.get(key) != null && !subjectData.get(key).isEmpty())
                .map(key -> key + "=" + subjectData.get(key))
                .collect(Collectors.joining(","));


        PKCS10CertificationRequestBuilder p10Builder = new JcaPKCS10CertificationRequestBuilder(
                new X500Principal(mapAsString), pair.getPublic());
        JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder("SHA256withRSA");
        ContentSigner signer = csBuilder.build(pair.getPrivate());
        GeneralName[] subjectAltNames = new GeneralName[]{
                new GeneralName(GeneralName.dNSName, "localhost"),
                new GeneralName(GeneralName.dNSName, "host.docker.internal")
                // TODO: with www ?? new GeneralName(GeneralName.dNSName, "www" + csrDTO.getCommonName())
        };

        Extension[] extensions = new Extension[] {
                Extension.create(Extension.subjectAlternativeName, true, new GeneralNames(subjectAltNames))
        };

        p10Builder.addAttribute(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest, new Extensions(extensions));

        PKCS10CertificationRequest csr = p10Builder.build(signer);

        StringWriter stringWriter = new StringWriter();
        JcaPEMWriter pemWriter = new JcaPEMWriter(stringWriter);
        pemWriter.writeObject(pair.getPrivate());
        pemWriter.close();

        FileWriter writer = new FileWriter(keyPath);
        writer.write(stringWriter.toString());
        writer.close();

        StringWriter builder = new StringWriter();
        pemWriter = new JcaPEMWriter(builder);
        pemWriter.writeObject(csr);
        pemWriter.close();

        HttpEntity<String> request = new HttpEntity<>(builder.toString());

        try {
            ResponseEntity<SecretCommunicationTokenDTO> token = ignoreSSLRestTemplate.exchange(
                    requestUrl,
                    HttpMethod.POST,
                    request,
                    SecretCommunicationTokenDTO.class);
            securityService.setSecurityToken(token.getBody());
        } catch (HttpClientErrorException exception) {
            throw new InvalidAPIResponse("Invalid API response.");
        }
    }

    @Override
    public CSRAutofillDataDTO getAutofillData(Principal principal) {
        HospitalConfigurationDTO configurationDTO = hospitalService.getConfiguration();
        HospitalUser user = this.hospitalUserService.getUser(principal.getName());
        return new CSRAutofillDataDTO(
                user.getLastName(),
                user.getFirstName(),
                user.getEmail(),
                configurationDTO.getOrganization(),
                configurationDTO.getOrganizationUnit(),
                configurationDTO.getCountry()
        );
    }

    private void setRDN(X500NameBuilder builder, ASN1ObjectIdentifier field, String value) {
        if (value != null && !value.isEmpty())
            builder.addRDN(field, value);
    }

    private KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(2048, random);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }
}
