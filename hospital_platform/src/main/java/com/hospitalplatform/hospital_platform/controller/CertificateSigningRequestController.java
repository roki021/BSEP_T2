package com.hospitalplatform.hospital_platform.controller;

import com.hospitalplatform.hospital_platform.dto.CSRAutofillDataDTO;
import com.hospitalplatform.hospital_platform.dto.CertificateSigningRequestDTO;
import com.hospitalplatform.hospital_platform.exception.JSONException;
import com.hospitalplatform.hospital_platform.exception.impl.InvalidAPIResponse;
import com.hospitalplatform.hospital_platform.service.CertificateSigningRequestService;
import org.bouncycastle.operator.OperatorCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("api/certificate-signing-requests")
public class CertificateSigningRequestController {
    @Autowired
    private CertificateSigningRequestService certificateSigningRequestService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> sendCertificateSigningRequest(@RequestBody @Validated CertificateSigningRequestDTO csr)
            throws OperatorCreationException, IOException, InvalidAPIResponse {
        System.out.println("Pristiglo: " + csr.getCommonName());
        certificateSigningRequestService.sendRequest(csr);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/autofill-data")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> getFormAutofillData(Principal principal) {
        CSRAutofillDataDTO autofillDataDTO = certificateSigningRequestService.getAutofillData(principal);
        return new ResponseEntity<>(autofillDataDTO, HttpStatus.OK);
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
