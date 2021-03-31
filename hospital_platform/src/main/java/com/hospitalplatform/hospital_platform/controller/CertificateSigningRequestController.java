package com.hospitalplatform.hospital_platform.controller;

import com.hospitalplatform.hospital_platform.dto.CertificateSigningRequestDTO;
import com.hospitalplatform.hospital_platform.exception.JSONException;
import com.hospitalplatform.hospital_platform.exception.impl.InvalidAPIResponse;
import com.hospitalplatform.hospital_platform.service.CertificateSigningRequestService;
import org.bouncycastle.operator.OperatorCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("api/")
public class CertificateSigningRequestController {
    @Autowired
    private CertificateSigningRequestService certificateSigningRequestService;

    @PostMapping("/certificate-signing-request")
    public ResponseEntity<?> sendCertificateSigningRequest(@RequestBody CertificateSigningRequestDTO csr)
            throws OperatorCreationException, IOException, InvalidAPIResponse {
        certificateSigningRequestService.sendRequest(csr);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @ExceptionHandler({ OperatorCreationException.class })
    public ResponseEntity<?> handleException() {
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ JSONException.class })
    public ResponseEntity<?> JSONHandleException(JSONException exception) {
        System.err.println("Exception [code: " + exception.getErrorCode() + "] " + exception.getMessage());
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
}
