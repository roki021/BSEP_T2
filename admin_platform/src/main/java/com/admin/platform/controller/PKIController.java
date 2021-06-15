package com.admin.platform.controller;

import com.admin.platform.constants.TemplateTypes;
import com.admin.platform.dto.*;
import com.admin.platform.exception.JSONException;
import com.admin.platform.exception.impl.UnexpectedSituation;
import com.admin.platform.model.CertificateSigningRequest;
import com.admin.platform.model.DigitalCertificate;
import com.admin.platform.model.RevokedCertificate;
import com.admin.platform.service.DigitalCertificateService;
import com.admin.platform.service.OCSPService;
import com.admin.platform.service.impl.CertificateSigningRequestServiceImpl;
import org.apache.coyote.Response;
import org.bouncycastle.asn1.cmc.RevokeRequest;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateParsingException;
import java.sql.SQLException;

@RestController
@RequestMapping("/api")
public class PKIController {
    @Autowired
    private CertificateSigningRequestServiceImpl csrService;

    @Autowired
    private DigitalCertificateService digitalCertificateService;

    @Autowired
    private OCSPService ocspService;

    @GetMapping("/certificate-signing-requests")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> getCertificateSigningRequests() {
        return new ResponseEntity<>(
                csrService.getAll().stream().filter(CertificateSigningRequest::isActive).map(CertificateSigningRequestDTO::new), HttpStatus.OK);
    }

    @GetMapping("/certificate-signing-requests/confirm/{csrId}")
    public ResponseEntity<?> confirmCertificateSigningRequest(@PathVariable Long csrId) {
        csrService.confirmCertificateSigningRequest(csrId);
        return new ResponseEntity<>("Request successfully confirmed!", HttpStatus.ACCEPTED);
    }

    @PostMapping("/external/certificate-signing-requests")
    public ResponseEntity<SecretCommunicationTokenDTO> receiveCertificateSigningRequest(
            @RequestBody byte[] request) throws IOException, UnexpectedSituation {

        return ResponseEntity.ok(csrService.saveRequest(request));
    }

    @PostMapping("/issue-certificate/{csrId}/{templateName}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> issueCertificate(@PathVariable Long csrId, @PathVariable String templateName) {
        try {
            csrService.logicRemove(csrId);
        } catch (SQLException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!templateName.equals(TemplateTypes.ROOT.toString()) &&
                !templateName.equals(TemplateTypes.HOSPITAL.toString()) &&
                !templateName.equals(TemplateTypes.DEVICE.toString())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(this.digitalCertificateService.createCertificate(csrId, TemplateTypes.valueOf(templateName)) == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/digital-certificates")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> getDigitalCertificates() {
        return new ResponseEntity<>(
                digitalCertificateService.getAll().stream().map(cert -> {
                    DigitalCertificateDTO dto = new DigitalCertificateDTO(cert);
                    RevokedCertificate rc = ocspService.getIfIsRevoked(
                            dto.getSerialNumber().longValue());
                    dto.setRevoked(rc != null);
                    return dto;
                }), HttpStatus.OK);
    }

    @GetMapping("/digital-certificates/validity/{serialNumber}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> checkValidity(@PathVariable Long serialNumber) {
        return new ResponseEntity<>(
                ocspService.getIfIsRevoked(serialNumber) != null, HttpStatus.OK);
    }

    @GetMapping("/digital-certificates/{serialNumber}")
    @PreAuthorize("hasAnyRole('ADMIN')")
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

            RevokedCertificate rc = ocspService.getIfIsRevoked(serialNumber);
            dto.setRevokeReason(rc != null ? rc.getRevokeReason() : null);
            return new ResponseEntity<>(
                    dto, HttpStatus.OK);
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/digital-certificates/revoke")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> revokeCertificate(@RequestBody @Validated RevokeRequestDTO request) {
        try {
            ocspService.revokeCertificate(request);
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
