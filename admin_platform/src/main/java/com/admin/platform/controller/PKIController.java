package com.admin.platform.controller;

import com.admin.platform.dto.CertificateSigningRequestDTO;
import com.admin.platform.dto.JSONExceptionMessage;
import com.admin.platform.exception.JSONException;
import com.admin.platform.exception.impl.UnexpectedSituation;
import com.admin.platform.service.CertificateSigningRequestService;
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
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
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
