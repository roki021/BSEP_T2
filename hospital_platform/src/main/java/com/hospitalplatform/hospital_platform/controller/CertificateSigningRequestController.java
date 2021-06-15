package com.hospitalplatform.hospital_platform.controller;

import com.hospitalplatform.hospital_platform.dto.CSRAutofillDataDTO;
import com.hospitalplatform.hospital_platform.dto.CertificateSigningRequestDTO;
import com.hospitalplatform.hospital_platform.exception.JSONException;
import com.hospitalplatform.hospital_platform.exception.impl.InvalidAPIResponse;
import com.hospitalplatform.hospital_platform.mercury.logger.Logger;
import com.hospitalplatform.hospital_platform.service.CertificateSigningRequestService;
import org.bouncycastle.operator.OperatorCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("api/certificate-signing-requests")
public class CertificateSigningRequestController {
    @Autowired
    private CertificateSigningRequestService certificateSigningRequestService;

    @Autowired
    @Qualifier("generalLogger")
    private Logger logger;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') && hasAuthority('CREATE_CSR_PRIVILEGE')")
    public ResponseEntity<?> sendCertificateSigningRequest(
            @RequestBody @Validated CertificateSigningRequestDTO csr, HttpServletRequest request)
            throws OperatorCreationException, IOException, InvalidAPIResponse {
        certificateSigningRequestService.sendRequest(csr);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.writeMessage(
                String.format("[SUCCESS] %s %s %s - ip %s",
                        simpleDateFormat.format(new Date()),
                        "api/certificate-signing-requests",
                        "SENDCSR",
                        request.getRemoteAddr()));

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/autofill-data")
    @PreAuthorize("hasRole('ADMIN') && hasAuthority('CREATE_CSR_PRIVILEGE')")
    public ResponseEntity<?> getFormAutofillData(Principal principal, HttpServletRequest request) {
        CSRAutofillDataDTO autofillDataDTO = certificateSigningRequestService.getAutofillData(principal);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.writeMessage(
                String.format("[INFO] %s %s %s - ip %s",
                        simpleDateFormat.format(new Date()),
                        "api/certificate-signing-requests/autofill",
                        "AUTOFILL",
                        request.getRemoteAddr()));

        return new ResponseEntity<>(autofillDataDTO, HttpStatus.OK);
    }

    @ExceptionHandler({ OperatorCreationException.class })
    public ResponseEntity<?> handleException(HttpServletRequest httpServletRequest) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.writeMessage(
                String.format("[ERROR] %s %s %s - ip %s",
                        simpleDateFormat.format(new Date()),
                        "api/certificate-signing-requests",
                        "CSREXCEPTION",
                        httpServletRequest.getRemoteAddr()));

        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ JSONException.class })
    public ResponseEntity<?> JSONHandleException(JSONException exception) {
        System.err.println("Exception [code: " + exception.getErrorCode() + "] " + exception.getMessage());
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
}
