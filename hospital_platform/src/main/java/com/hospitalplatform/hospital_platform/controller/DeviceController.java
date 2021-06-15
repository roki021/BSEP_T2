package com.hospitalplatform.hospital_platform.controller;

import com.hospitalplatform.hospital_platform.dto.CertificateSigningRequestDTO;
import com.hospitalplatform.hospital_platform.dto.DeviceDTO;
import com.hospitalplatform.hospital_platform.exception.impl.InvalidAPIResponse;
import com.hospitalplatform.hospital_platform.exception.impl.SQLConflict;
import com.hospitalplatform.hospital_platform.mercury.logger.Logger;
import com.hospitalplatform.hospital_platform.service.CertificateSigningRequestService;
import com.hospitalplatform.hospital_platform.service.DeviceService;
import org.bouncycastle.operator.OperatorCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/devices")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;

    @Autowired
    private CertificateSigningRequestService certificateSigningRequestService;

    @Autowired
    @Qualifier("deviceLogger")
    private Logger deviceLogger;

    @Autowired
    @Qualifier("generalLogger")
    private Logger logger;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') || (hasRole('DOCTOR') && hasAuthority('READ_DEVICES_PRIVILEGE'))")
    public ResponseEntity<List<DeviceDTO>> getAll(HttpServletRequest request) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.writeMessage(
                String.format("[INFO] %s %s %s - ip %s",
                        simpleDateFormat.format(new Date()),
                        "api/devices",
                        "GETDEVICES",
                        request.getRemoteAddr()));

        return ResponseEntity.ok(deviceService.getAll());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') && hasAuthority('WRITE_DEVICES_PRIVILEGE')")
    public ResponseEntity<?> addNew(@RequestBody DeviceDTO deviceDTO, Principal user, HttpServletRequest request) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            DeviceDTO saved = deviceService.addDevice(deviceDTO);
            deviceService.forwardRequest(deviceService.getDeviceCsr(saved,
                    certificateSigningRequestService.getAutofillData(user)), true);

            logger.writeMessage(
                    String.format("[SUCCESS] %s %s %s - ip %s",
                            simpleDateFormat.format(new Date()),
                            "api/devices",
                            "ADDDEVICES",
                            request.getRemoteAddr()));

            return ResponseEntity.ok(saved);
        } catch(SQLConflict | InvalidAPIResponse exception) {
            logger.writeMessage(
                    String.format("[WARNING] %s %s %s - ip %s",
                            simpleDateFormat.format(new Date()),
                            "api/devices",
                            "DEVICEERROR",
                            request.getRemoteAddr()));

            System.err.println("Exception [code: " + exception.getErrorCode() + "] " + exception.getMessage());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/receive")
    public ResponseEntity<?> receiveMessage(
            @RequestBody String message, @RequestHeader("Token") String token, HttpServletRequest request) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if(deviceService.isValidToken(token)) {
            deviceLogger.writeMessage(message);

            logger.writeMessage(
                    String.format("[SUCCESS] %s %s %s - ip %s",
                            simpleDateFormat.format(new Date()),
                            "api/devices",
                            "DEVICEMESSAGE",
                            request.getRemoteAddr()));

            return new ResponseEntity<>(HttpStatus.OK);
        }

        logger.writeMessage(
                String.format("[WARNING] %s %s %s - ip %s",
                        simpleDateFormat.format(new Date()),
                        "api/devices",
                        "DEVICEUNAUTHORIZED",
                        request.getRemoteAddr()));

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
