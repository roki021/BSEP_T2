package com.hospitalplatform.hospital_platform.controller;

import com.hospitalplatform.hospital_platform.dto.NewMemberDTO;
import com.hospitalplatform.hospital_platform.dto.RoleUpdateDTO;
import com.hospitalplatform.hospital_platform.mercury.logger.Logger;
import com.hospitalplatform.hospital_platform.service.HospitalUserService;
import com.hospitalplatform.hospital_platform.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@RestController
@RequestMapping("/api/external/members")
public class HospitalMemberController {
    @Autowired
    private HospitalUserService hospitalUserService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    @Qualifier("generalLogger")
    private Logger logger;

    @PostMapping
    public ResponseEntity<?> createMember(@RequestHeader("X-sudo-key") String key,
                                          @RequestBody @Validated NewMemberDTO member,
                                          HttpServletRequest request) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (!securityService.checkSecurityToken(key)) {
            logger.writeMessage(
                    String.format("[ERROR] %s %s %s - ip %s",
                            simpleDateFormat.format(new Date()),
                            "api/external/members",
                            "INVALIDKEY",
                            request.getRemoteAddr()));
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        try {
            hospitalUserService.createUser(member);
        } catch (Exception e) {
            e.printStackTrace();
            HashMap<String, String> message = new HashMap<>() {{
                put("error", e.getMessage());
            }};

            logger.writeMessage(
                    String.format("[INFO] %s %s %s - ip %s",
                            simpleDateFormat.format(new Date()),
                            "api/external/members",
                            "CREATEMEMBERERROR",
                            request.getRemoteAddr()));

            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        logger.writeMessage(
                String.format("[SUCCESS] %s %s %s - ip %s",
                        simpleDateFormat.format(new Date()),
                        "api/external/members",
                        "CREATEDMEMBER",
                        request.getRemoteAddr()));

        return new ResponseEntity(null, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<?>  deleteMember(@RequestHeader("X-sudo-key") String key,
                                           @PathVariable Integer memberId, HttpServletRequest request) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (!securityService.checkSecurityToken(key)) {
            logger.writeMessage(
                    String.format("[ERROR] %s %s %s - ip %s",
                            simpleDateFormat.format(new Date()),
                            "api/external/members",
                            "INVALIDKEY",
                            request.getRemoteAddr()));

            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        hospitalUserService.deleteUser(memberId);

        logger.writeMessage(
                String.format("[SUCCESS] %s %s %s - ip %s",
                        simpleDateFormat.format(new Date()),
                        "api/external/members",
                        "REMOVEDMEMBER",
                        request.getRemoteAddr()));

        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<?> getMembers(@RequestHeader("X-sudo-key") String key, HttpServletRequest request) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (!securityService.checkSecurityToken(key)) {
            logger.writeMessage(
                    String.format("[ERROR] %s %s %s - ip %s",
                            simpleDateFormat.format(new Date()),
                            "api/external/members",
                            "INVALIDKEY",
                            request.getRemoteAddr()));

            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        logger.writeMessage(
                String.format("[INFO] %s %s %s - ip %s",
                        simpleDateFormat.format(new Date()),
                        "api/external/members",
                        "GETMEMBERS",
                        request.getRemoteAddr()));

        return new ResponseEntity<>(hospitalUserService.getHospitalUsers(), HttpStatus.OK);
    }

    @PutMapping("/{memberId}/roles")
    public ResponseEntity<?> changeMemberRole(@RequestHeader("X-sudo-key") String key,
                                              @PathVariable Integer memberId,
                                              @RequestBody @Validated RoleUpdateDTO roleUpdateDTO,
                                              HttpServletRequest request) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (!securityService.checkSecurityToken(key)) {
            logger.writeMessage(
                    String.format("[ERROR] %s %s %s - ip %s",
                            simpleDateFormat.format(new Date()),
                            "api/external/members",
                            "INVALIDKEY",
                            request.getRemoteAddr()));

            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        hospitalUserService.changeUserRole(memberId, roleUpdateDTO);

        logger.writeMessage(
                String.format("[SUCCESS] %s %s %s - ip %s",
                        simpleDateFormat.format(new Date()),
                        "api/external/members",
                        "ROLECHANGED",
                        request.getRemoteAddr()));

        return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<?> handleException(HttpServletRequest request) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.writeMessage(
                String.format("[ERROR] %s %s %s - ip %s",
                        simpleDateFormat.format(new Date()),
                        "api/external/members",
                        "HOSPITALEXCEPTION",
                        request.getRemoteAddr()));

        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
}
