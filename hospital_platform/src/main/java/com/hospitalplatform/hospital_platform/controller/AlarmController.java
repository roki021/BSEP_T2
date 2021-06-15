package com.hospitalplatform.hospital_platform.controller;

import com.hospitalplatform.hospital_platform.dto.AlarmDTO;
import com.hospitalplatform.hospital_platform.exception.impl.SQLConflict;
import com.hospitalplatform.hospital_platform.exception.impl.UnexpectedSituation;
import com.hospitalplatform.hospital_platform.mercury.alarm.constants.ActivationTag;
import com.hospitalplatform.hospital_platform.models.HospitalUser;
import com.hospitalplatform.hospital_platform.mercury.logger.Logger;
import com.hospitalplatform.hospital_platform.service.AlarmService;
import com.hospitalplatform.hospital_platform.service.HospitalUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("api/alarms")
public class AlarmController {

    @Autowired
    private AlarmService alarmService;

    @Autowired
    private HospitalUserService hospitalUserService;

    @Autowired
    @Qualifier("generalLogger")
    private Logger logger;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<?> getAll(HttpServletRequest request, Principal principal) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.writeMessage(
                String.format("[INFO] %s %s %s - ip %s",
                        simpleDateFormat.format(new Date()),
                        "api/alarms",
                        "GETALARMS",
                        request.getRemoteAddr()));

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
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<?> addAlarm(@RequestBody AlarmDTO alarmDTO, HttpServletRequest request, Principal principal) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            logger.writeMessage(
                    String.format("[INFO] %s %s %s - ip %s",
                            simpleDateFormat.format(new Date()),
                            "api/alarms",
                            "ADDALARM",
                            request.getRemoteAddr()));

            HospitalUser user = hospitalUserService.getUser(principal.getName());
            if(user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN")))
                alarmDTO.setActivationTags(
                        ActivationTag.SEC.getTag() | ActivationTag.LOG_SIMULATOR.getTag());

            if(user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_DOCTOR")))
                alarmDTO.setActivationTags(
                        ActivationTag.DEVICE.getTag());

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
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
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

    @GetMapping("{id}/notifications")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<?> getAllAlarmNotification(
            @PathVariable Integer id, HttpServletRequest request) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.writeMessage(
                String.format("[INFO] %s %s %s - ip %s",
                        simpleDateFormat.format(new Date()),
                        "api/alarms/" + id + "/notifications",
                        "GETALARMNOTIFICATIONS",
                        request.getRemoteAddr()));

        return new ResponseEntity<>(alarmService.getAlarmNotifications(id), HttpStatus.OK);
    }
}
