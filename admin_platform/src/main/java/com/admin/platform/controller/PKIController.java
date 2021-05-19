package com.admin.platform.controller;

import com.admin.platform.constants.TemplateTypes;
import com.admin.platform.dto.*;
import com.admin.platform.exception.JSONException;
import com.admin.platform.exception.impl.UnexpectedSituation;
import com.admin.platform.model.DigitalCertificate;
import com.admin.platform.service.DigitalCertificateService;
import com.admin.platform.service.impl.CertificateSigningRequestServiceImpl;
import org.bouncycastle.asn1.cmc.RevokeRequest;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigInteger;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateParsingException;

@RestController
@RequestMapping("/api")
public class PKIController {
    @Autowired
    private CertificateSigningRequestServiceImpl csrService;

    @Autowired
    private DigitalCertificateService digitalCertificateService;

    @GetMapping("/certificate-signing-requests")
    public ResponseEntity<?> getCertificateSigningRequests() {
        return new ResponseEntity<>(
                csrService.getAll().stream().map(csr -> new CertificateSigningRequestDTO(csr)), HttpStatus.OK);
    }

    @GetMapping("/certificate-signing-requests/confirm/{csrId}")
    public ResponseEntity<?> confirmCertificateSigningRequest(@PathVariable Long csrId) {
        csrService.confirmCertificateSigningRequest(csrId);
        return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
    }

    @PostMapping("/external/certificate-signing-requests")
    public ResponseEntity<?> receiveCertificateSigningRequest(
            @RequestBody byte[] request) throws IOException, UnexpectedSituation {

        csrService.saveRequest(request);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/issue-certificate/{csrId}/{templateName}")
    public ResponseEntity<?> issueCertificate(@PathVariable Long csrId, @PathVariable String templateName) {
        //TODO: .. remove crs?

        if (!templateName.equals(TemplateTypes.ROOT.toString()) &&
                !templateName.equals(TemplateTypes.LEAF_HOSPITAL.toString())) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        this.digitalCertificateService.createCertificate(csrId, TemplateTypes.valueOf(templateName));
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/digital-certificates")
    public ResponseEntity<?> getDigitalCertificates() {
        return new ResponseEntity<>(
                digitalCertificateService.getAll().stream().map(cert -> {
                    DigitalCertificateDTO dto = new DigitalCertificateDTO(cert);
                    dto.setRevoked(
                            digitalCertificateService.isRevoked(
                                    dto.getSerialNumber().longValue()));
                    return dto;
                }), HttpStatus.OK);
    }

    @GetMapping("/digital-certificates/validity/{serialNumber}")
    public ResponseEntity<?> checkValidity(@PathVariable Long serialNumber) {
        return new ResponseEntity<>(
                digitalCertificateService.isRevoked(serialNumber), HttpStatus.OK);
    }

    @GetMapping("/digital-certificates/{serialNumber}")
    public ResponseEntity<?> getCertificate(@PathVariable Long serialNumber) {
        DigitalCertificate dc = digitalCertificateService.
                getBySerialNumber(new BigInteger(serialNumber.toString()));

        try {
            CertificateInstanceDTO dto = new CertificateInstanceDTO(
                    digitalCertificateService.getSubjectName(serialNumber));
            dto.setStartFrom(dc.getStartDate());
            dto.setEndTo(dc.getEndDate());
            dto.setSerialNumber(dc.getSerialNumber().longValue());
            dto.setKeyUsage(digitalCertificateService.getCertKeyUsage(serialNumber));
            return new ResponseEntity<>(
                    dto, HttpStatus.OK);
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/digital-certificates/revoke")
    public ResponseEntity<?> revokeCertificate(@RequestBody RevokeRequestDTO request) {
        try {
            digitalCertificateService.revokeCertificate(request);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler({ IOException.class, ConstraintViolationException.class })
    public ResponseEntity<?> handleBadRequestException() {
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ JSONException.class })
    public ResponseEntity<?> handleJSONException(JSONException exception) {
        return new ResponseEntity<>(
                new JSONExceptionMessage(exception.getErrorCode(), exception.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
