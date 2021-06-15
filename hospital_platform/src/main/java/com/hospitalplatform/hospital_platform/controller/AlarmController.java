package com.hospitalplatform.hospital_platform.controller;

import com.hospitalplatform.hospital_platform.dto.AlarmDTO;
import com.hospitalplatform.hospital_platform.exception.impl.SQLConflict;
import com.hospitalplatform.hospital_platform.exception.impl.UnexpectedSituation;
import com.hospitalplatform.hospital_platform.mercury.alarm.constants.ActivationTag;
import com.hospitalplatform.hospital_platform.models.HospitalUser;
import com.hospitalplatform.hospital_platform.service.AlarmService;
import com.hospitalplatform.hospital_platform.service.HospitalUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/alarms")
public class AlarmController {

    @Autowired
    private AlarmService alarmService;

    @Autowired
    private HospitalUserService hospitalUserService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR') && hasAuthority('READ_ALARMS_PRIVILEGE') ")
    public ResponseEntity<?> getAll(Principal principal) {
        HospitalUser user = hospitalUserService.getUser(principal.getName());
        if(user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN")))
            return ResponseEntity.ok(alarmService.getAllByTags(
                    ActivationTag.SEC.getTag() | ActivationTag.LOG_SIMULATOR.getTag()
            ));

        if(user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_DOCTOR")))
            return ResponseEntity.ok(alarmService.getAllByTags(
                    ActivationTag.DEVICE.getTag()
            ));

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR') && hasAuthority('WRITE_ALARMS_PRIVILEGE')")
    public ResponseEntity<?> addAlarm(@RequestBody AlarmDTO alarmDTO, Principal principal) {
        try {
            HospitalUser user = hospitalUserService.getUser(principal.getName());
            if(user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN")))
                alarmDTO.setActivationTags(
                        ActivationTag.SEC.getTag() | ActivationTag.LOG_SIMULATOR.getTag());

            if(user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_DOCTOR")))
                alarmDTO.setActivationTags(
                        ActivationTag.DEVICE.getTag());

            return ResponseEntity.ok(alarmService.addAlarm(alarmDTO));
        } catch (SQLConflict exception) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR') && hasAuthority('DELETE_ALARMS_PRIVILEGE')")
    public ResponseEntity<?> removeAlarm(@PathVariable Long id) {
        try {
            alarmService.removeAlarm(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UnexpectedSituation exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
