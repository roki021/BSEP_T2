package com.hospitalplatform.hospital_platform.controller;

import com.hospitalplatform.hospital_platform.dto.NewMemberDTO;
import com.hospitalplatform.hospital_platform.dto.RoleUpdateDTO;
import com.hospitalplatform.hospital_platform.service.HospitalUserService;
import com.hospitalplatform.hospital_platform.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

@RestController
@RequestMapping("/api/external/members")
public class HospitalMemberController {
    @Autowired
    private HospitalUserService hospitalUserService;

    @Autowired
    private SecurityService securityService;

    @PostMapping
    public ResponseEntity<?> createMember(@RequestHeader("X-sudo-key") String key,
                                          @RequestBody @Validated NewMemberDTO member) throws Exception {
        /*if (!securityService.checkSecurityToken(key))
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);*/
        hospitalUserService.createUser(member);
        return new ResponseEntity(null, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<?>  deleteMember(@RequestHeader("X-sudo-key") String key,
                                           @PathVariable Integer memberId) {
        if (!securityService.checkSecurityToken(key))
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        hospitalUserService.deleteUser(memberId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<?> getMembers(@RequestHeader("X-sudo-key") String key) {
        if (!securityService.checkSecurityToken(key))
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(hospitalUserService.getHospitalUsers(), HttpStatus.OK);
    }

    @PutMapping("/{memberId}/roles")
    public ResponseEntity<?> changeMemberRole(@RequestHeader("X-sudo-key") String key,
                                              @PathVariable Integer memberId,
                                              @RequestBody @Validated RoleUpdateDTO roleUpdateDTO) {
        if (!securityService.checkSecurityToken(key))
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        hospitalUserService.changeUserRole(memberId, roleUpdateDTO);
        return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<?> handleException() {
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
}
