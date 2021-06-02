package com.admin.platform.controller;

import com.admin.platform.dto.NewMemberDTO;
import com.admin.platform.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
        return new ResponseEntity(hospitalService.getHospitalMembers(hospitalId), HttpStatus.OK);
    }
}
