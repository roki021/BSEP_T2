package com.hospitalplatform.hospital_platform.controller;

import com.hospitalplatform.hospital_platform.dto.LoginDTO;
import com.hospitalplatform.hospital_platform.dto.UserTokenStateDTO;
import com.hospitalplatform.hospital_platform.mercury.logger.Logger;
import com.hospitalplatform.hospital_platform.mercury.logger.impl.LogSimulatorLogger;
import com.hospitalplatform.hospital_platform.models.HospitalUser;
import com.hospitalplatform.hospital_platform.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;

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
    public ResponseEntity<UserTokenStateDTO> login(
            @RequestBody @Validated LoginDTO loginDTO,
            HttpServletResponse response,
            HttpServletRequest request) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // quick
        if (hospitalUserService.isUserLocked(loginDTO.getUsername())) {
            logger.writeMessage(
                    String.format("[INFO] %s %s %s - username %s ip %s",
                            simpleDateFormat.format(new Date()),
                            "api/login",
                            "LOGINLOCKED",
                            loginDTO.getUsername(),
                            request.getRemoteAddr()));
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }

        UserTokenStateDTO token = authService.loginUser(loginDTO);

        if (token == null) {
            logger.writeMessage(
                    String.format("[WARNING] %s %s %s - username %s ip %s",
                            simpleDateFormat.format(new Date()),
                            "api/login",
                            "LOGINFAIL",
                            loginDTO.getUsername(),
                            request.getRemoteAddr()));
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }

        hospitalUserService.updateLastAccess(loginDTO.getUsername());
        logger.writeMessage(
                String.format("[SUCCESS] %s %s %s - username %s ip %s",
                        simpleDateFormat.format(new Date()),
                        "api/login",
                        "LOGINSUCCESS",
                        loginDTO.getUsername(),
                        request.getRemoteAddr()));

        return ResponseEntity.ok(token);
    }
}
