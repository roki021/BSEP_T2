package com.admin.platform.controller;

import com.admin.platform.constants.TemplateTypes;
import com.admin.platform.dto.CertificateSigningRequestDTO;
import com.admin.platform.dto.DigitalCertificateDTO;
import com.admin.platform.dto.JSONExceptionMessage;
import com.admin.platform.exception.JSONException;
import com.admin.platform.exception.impl.UnexpectedSituation;
import com.admin.platform.service.CertificateSigningRequestService;
import com.admin.platform.service.DigitalCertificateService;
import com.admin.platform.service.impl.CertificateSigningRequestServiceImpl;
import org.apache.coyote.Response;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

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
                digitalCertificateService.getAll().stream().map(DigitalCertificateDTO::new), HttpStatus.OK);
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
