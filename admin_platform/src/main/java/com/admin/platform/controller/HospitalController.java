package com.admin.platform.controller;

import com.admin.platform.dto.*;
import com.admin.platform.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@RestController
@RequestMapping("/api/hospitals")
public class HospitalController {
    @Autowired
    private HospitalService hospitalService;

    @GetMapping
    public ResponseEntity<?> getHospitals() {
        return new ResponseEntity<>(hospitalService.getHospitals(), HttpStatus.OK);
    }

    @PostMapping("/{hospitalId}/members")
    public ResponseEntity<?> createMember(
            @PathVariable Integer hospitalId, @RequestBody @Validated NewMemberDTO newMemberDTO) {
        try {
            hospitalService.addHospitalMember(hospitalId, newMemberDTO);
        } catch (Exception exception) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{hospitalId}")
    public ResponseEntity<?> getMembers(@PathVariable Integer hospitalId) throws Exception {
        try {
            return new ResponseEntity(hospitalService.getHospitalMembers(hospitalId), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/{hospitalId}/members/{memberId}")
    public ResponseEntity<?> deleteMember(@PathVariable Integer hospitalId, @PathVariable Integer memberId) {
        try {
            hospitalService.deleteHospitalMember(hospitalId, memberId);
        } catch (Exception exception) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{hospitalId}/members/{memberId}/roles")
    public ResponseEntity<?> changeMemberRole(
            @PathVariable Integer hospitalId,
            @PathVariable Integer memberId,
            @RequestBody @Validated RoleUpdateDTO roleUpdateDTO) {
        try {
            hospitalService.changeHospitalMemberRole(hospitalId, memberId, roleUpdateDTO);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{hospitalId}")
    public ResponseEntity<?> sendLoggerConfig(
            @PathVariable Integer hospitalId, @RequestBody @Validated LoggersDTO loggersDTO) {
        for (LoggerDTO l : loggersDTO.getLoggers()) {
            for (ParamDTO p : l.getParams()) {
                if (p.getRegex().contains("(") || p.getRegex().contains(")"))
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                try {
                    Pattern.compile(p.getRegex());
                } catch (PatternSyntaxException exception) {
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                }
            }
        }

        try {
            hospitalService.sendLoggerConfigurationToAdministration(hospitalId, loggersDTO);
        } catch (Exception exception) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
