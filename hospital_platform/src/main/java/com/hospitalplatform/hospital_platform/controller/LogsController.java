package com.hospitalplatform.hospital_platform.controller;

import com.hospitalplatform.hospital_platform.mercury.message.Message;
import com.hospitalplatform.hospital_platform.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class LogsController {
    @Autowired
    private MessageService messageService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Message>> getLogs() {
        return new ResponseEntity<>(messageService.getAllLogs(), HttpStatus.OK);
    }
}
