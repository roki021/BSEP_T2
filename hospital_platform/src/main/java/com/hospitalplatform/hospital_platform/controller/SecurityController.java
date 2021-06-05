package com.hospitalplatform.hospital_platform.controller;

import com.hospitalplatform.hospital_platform.dto.CertificateSigningRequestDTO;
import com.hospitalplatform.hospital_platform.exception.impl.InvalidAPIResponse;
import com.hospitalplatform.hospital_platform.service.SecurityService;
import org.bouncycastle.operator.OperatorCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/security")
public class SecurityController {
    @Autowired
    private SecurityService service;

    @GetMapping("/token")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> getToken()
            throws OperatorCreationException, IOException, InvalidAPIResponse {
        return ResponseEntity.ok(service.getSecurityToken());
    }

    @PostMapping("/token/activate")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> activateToken() throws Exception {
        service.changeSecurityTokenStatus(true);
        return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
    }

    @PostMapping("/token/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> deactivateToken() throws Exception {
        service.changeSecurityTokenStatus(false);
        return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
    }
}
