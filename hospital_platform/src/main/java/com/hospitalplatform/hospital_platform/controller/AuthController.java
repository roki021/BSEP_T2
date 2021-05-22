package com.hospitalplatform.hospital_platform.controller;

import com.hospitalplatform.hospital_platform.dto.LoginDTO;
import com.hospitalplatform.hospital_platform.dto.UserTokenStateDTO;
import com.hospitalplatform.hospital_platform.models.HospitalUser;
import com.hospitalplatform.hospital_platform.service.AuthService;
import com.hospitalplatform.hospital_platform.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<UserTokenStateDTO> login(@RequestBody @Validated LoginDTO loginDTO, HttpServletResponse response) {
        System.out.println(loginDTO.getUsername());
        System.out.println(loginDTO.getPassword());
        UserTokenStateDTO token = authService.loginUser(loginDTO);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/hello-doctor")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<String> helloDoctor() {
        return new ResponseEntity<>("Hello Doctor", HttpStatus.OK);
    }

    @PostMapping("/hello-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> helloAdmin() {
        return new ResponseEntity<>("Hello Admin", HttpStatus.OK);
    }
}
