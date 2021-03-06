package com.hospitalplatform.hospital_platform.controller;

import com.hospitalplatform.hospital_platform.dto.IntermediateToken;
import com.hospitalplatform.hospital_platform.dto.LoginDTO;
import com.hospitalplatform.hospital_platform.dto.UserTokenStateDTO;
import com.hospitalplatform.hospital_platform.mercury.logger.Logger;
import com.hospitalplatform.hospital_platform.service.AuthService;
import com.hospitalplatform.hospital_platform.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private HospitalUserService hospitalUserService;

    @Autowired
    private BlacklistService blacklistService;

    @Autowired
    @Qualifier("authLogger")
    private Logger logger;

    @PostMapping("/login")
    public ResponseEntity<UserTokenStateDTO> login(@RequestBody @Validated LoginDTO loginDTO, HttpServletResponse response, HttpServletRequest request) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (blacklistService.contains(request.getRemoteAddr())) {
            logger.writeMessage(
                    String.format("[WARNING] %s %s %s - username %s ip %s",
                            simpleDateFormat.format(new Date()),
                            "api/login",
                            "IPBLOCKED",
                            loginDTO.getUsername(),
                            request.getRemoteAddr()));
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }

        // quick
        if (hospitalUserService.isUserLocked(loginDTO.getUsername())) {
            logger.writeMessage(
                    String.format("[WARNING] %s %s %s - username %s ip %s",
                            simpleDateFormat.format(new Date()),
                            "api/login",
                            "LOGINLOCKED",
                            loginDTO.getUsername(),
                            request.getRemoteAddr()));
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }

        IntermediateToken token = null;
        try {
            token = authService.loginUser(loginDTO);
            if (token == null) {
                logger.writeMessage(
                        String.format("[WARNING] %s %s %s- username %s ip %s",
                                simpleDateFormat.format(new Date()),
                                "api/login",
                                "NULLTOKEN",
                                loginDTO.getUsername(),
                                request.getRemoteAddr()));
                return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
            }
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        HttpHeaders headers = new HttpHeaders();

        hospitalUserService.updateLastAccess(loginDTO.getUsername());
        logger.writeMessage(
                String.format("[SUCCESS] %s %s %s - username %s ip %s",
                        simpleDateFormat.format(new Date()),
                        "api/login",
                        "LOGINSUCCESS",
                        loginDTO.getUsername(),
                        request.getRemoteAddr()));

        headers.add(
                "Set-Cookie",
                "__Secure-Fgp=" + token.getFingerprint() + "; Path=/; SameSite=Strict; HttpOnly; Secure");

        return new ResponseEntity<UserTokenStateDTO>(token.getUserTokenStateDTO(), headers, HttpStatus.OK);
    }
}
