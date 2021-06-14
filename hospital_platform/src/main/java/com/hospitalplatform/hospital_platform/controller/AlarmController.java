package com.hospitalplatform.hospital_platform.controller;

import com.hospitalplatform.hospital_platform.dto.AlarmDTO;
import com.hospitalplatform.hospital_platform.exception.impl.SQLConflict;
import com.hospitalplatform.hospital_platform.exception.impl.UnexpectedSituation;
import com.hospitalplatform.hospital_platform.service.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/alarms")
public class AlarmController {

    @Autowired
    private AlarmService alarmService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(alarmService.getAll());
    }

    @PostMapping
    public ResponseEntity<?> addAlarm(@RequestBody AlarmDTO alarmDTO) {
        try {
            return ResponseEntity.ok(alarmService.addAlarm(alarmDTO));
        } catch (SQLConflict exception) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeAlarm(@PathVariable Long id) {
        try {
            alarmService.removeAlarm(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UnexpectedSituation exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
