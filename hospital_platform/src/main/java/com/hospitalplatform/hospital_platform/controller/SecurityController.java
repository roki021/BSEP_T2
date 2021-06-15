package com.hospitalplatform.hospital_platform.controller;

import com.hospitalplatform.hospital_platform.dto.CertificateSigningRequestDTO;
import com.hospitalplatform.hospital_platform.exception.impl.InvalidAPIResponse;
import com.hospitalplatform.hospital_platform.mercury.logger.Logger;
import com.hospitalplatform.hospital_platform.service.SecurityService;
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
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api/security")
public class SecurityController {
    @Autowired
    private SecurityService service;

    @Autowired
    @Qualifier("generalLogger")
    private Logger logger;

    @GetMapping("/token")
    @PreAuthorize("hasAnyRole('ADMIN') && hasAuthority('READ_TOKEN_STATUS_PRIVILEGE')")
    public ResponseEntity<?> getToken(HttpServletRequest request)
            throws OperatorCreationException, IOException, InvalidAPIResponse {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.writeMessage(
                String.format("[INFO] %s %s %s - ip %s",
                        simpleDateFormat.format(new Date()),
                        "api/security",
                        "GETTOKEN",
                        request.getRemoteAddr()));

        return ResponseEntity.ok(service.getSecurityToken());
    }

    @PostMapping("/token/activate")
    @PreAuthorize("hasAnyRole('ADMIN') && hasAuthority('CHANGE_TOKEN_STATUS_PRIVILEGE')")
    public ResponseEntity<Void> activateToken(HttpServletRequest request) throws Exception {
        service.changeSecurityTokenStatus(true);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.writeMessage(
                String.format("[SUCCESS] %s %s %s - ip %s",
                        simpleDateFormat.format(new Date()),
                        "api/security",
                        "ACTIVATETOKEN",
                        request.getRemoteAddr()));

        return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
    }

    @PostMapping("/token/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN') && hasAuthority('CHANGE_TOKEN_STATUS_PRIVILEGE')")
    public ResponseEntity<Void> deactivateToken(HttpServletRequest request) throws Exception {
        service.changeSecurityTokenStatus(false);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.writeMessage(
                String.format("[SUCCESS] %s %s %s - ip %s",
                        simpleDateFormat.format(new Date()),
                        "api/security",
                        "DEACTIVATETOKEN",
                        request.getRemoteAddr()));

        return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<?> handleException(HttpServletRequest request) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.writeMessage(
                String.format("[ERROR] %s %s %s - ip %s",
                        simpleDateFormat.format(new Date()),
                        "api/security",
                        "TOKENEXCEPTION",
                        request.getRemoteAddr()));

        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
}
