package com.hospitalplatform.hospital_platform.controller;

import com.hospitalplatform.hospital_platform.dto.NewMemberDTO;
import com.hospitalplatform.hospital_platform.service.HospitalUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/external/members")
public class HospitalMemberController {
    @Autowired
    private HospitalUserService hospitalUserService;

    @PostMapping
    public ResponseEntity<?> createMember(@RequestBody @Validated NewMemberDTO member) throws Exception {
        hospitalUserService.createUser(member);
        return new ResponseEntity(null, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<?>  deleteMember(@PathVariable Integer memberId) {
        hospitalUserService.deleteUser(memberId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<?> getMembers() {
        return new ResponseEntity<>(hospitalUserService.getHospitalUsers(), HttpStatus.OK);
    }
}
