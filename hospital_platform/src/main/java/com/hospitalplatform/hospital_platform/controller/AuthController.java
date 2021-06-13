package com.hospitalplatform.hospital_platform.controller;

import com.hospitalplatform.hospital_platform.dto.IntermediateToken;
import com.hospitalplatform.hospital_platform.dto.LoginDTO;
import com.hospitalplatform.hospital_platform.dto.UserTokenStateDTO;
import com.hospitalplatform.hospital_platform.models.HospitalUser;
import com.hospitalplatform.hospital_platform.service.AuthService;
import com.hospitalplatform.hospital_platform.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<UserTokenStateDTO> login(@RequestBody @Validated LoginDTO loginDTO, HttpServletResponse response) {
        IntermediateToken token = null;
        try {
            token = authService.loginUser(loginDTO);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        HttpHeaders headers = new HttpHeaders();

        //TODO: fore debug "_Secure-Fgp=" + token.getFingerprint() + "; Path=/; HttpOnly" (change in filter also! __)

        headers.add(
                "Set-Cookie",
                "__Secure-Fgp=" + token.getFingerprint() + "; Path=/; SameSite=Strict; HttpOnly; Secure");

        return new ResponseEntity<UserTokenStateDTO>(token.getUserTokenStateDTO(), headers, HttpStatus.OK);
    }
}
