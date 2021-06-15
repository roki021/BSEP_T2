package com.hospitalplatform.hospital_platform.controller;

import com.hospitalplatform.hospital_platform.dto.AlarmDTO;
import com.hospitalplatform.hospital_platform.exception.impl.SQLConflict;
import com.hospitalplatform.hospital_platform.exception.impl.UnexpectedSituation;
import com.hospitalplatform.hospital_platform.mercury.logger.Logger;
import com.hospitalplatform.hospital_platform.service.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("api/alarms")
public class AlarmController {

    @Autowired
    private AlarmService alarmService;

    @Autowired
    @Qualifier("generalLogger")
    private Logger logger;

    @GetMapping
    public ResponseEntity<?> getAll(HttpServletRequest request) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.writeMessage(
                String.format("[INFO] %s %s %s - ip %s",
                        simpleDateFormat.format(new Date()),
                        "api/alarms",
                        "GETALARMS",
                        request.getRemoteAddr()));

        return ResponseEntity.ok(alarmService.getAll());
    }

    @PostMapping
    public ResponseEntity<?> addAlarm(@RequestBody AlarmDTO alarmDTO, HttpServletRequest request) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            logger.writeMessage(
                    String.format("[INFO] %s %s %s - ip %s",
                            simpleDateFormat.format(new Date()),
                            "api/alarms",
                            "ADDALARM",
                            request.getRemoteAddr()));

            return ResponseEntity.ok(alarmService.addAlarm(alarmDTO));
        } catch (SQLConflict exception) {
            logger.writeMessage(
                    String.format("[ERROR] %s %s %s - ip %s",
                            simpleDateFormat.format(new Date()),
                            "api/alarms",
                            "ARMCONFLICT",
                            request.getRemoteAddr()));

            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeAlarm(@PathVariable Long id, HttpServletRequest request) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            alarmService.removeAlarm(id);

            logger.writeMessage(
                    String.format("[INFO] %s %s %s - ip %s",
                            simpleDateFormat.format(new Date()),
                            "api/alarms",
                            "REMOVEALARM",
                            request.getRemoteAddr()));

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UnexpectedSituation exception) {
            logger.writeMessage(
                    String.format("[WARNING] %s %s %s - ip %s",
                            simpleDateFormat.format(new Date()),
                            "api/alarms",
                            "REMOVENOALARM",
                            request.getRemoteAddr()));

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
