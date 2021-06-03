package com.hospitalplatform.hospital_platform.controller;

import com.hospitalplatform.hospital_platform.dto.DeviceDTO;
import com.hospitalplatform.hospital_platform.exception.impl.SQLConflict;
import com.hospitalplatform.hospital_platform.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/devices")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;

    @GetMapping
    public ResponseEntity<List<DeviceDTO>> getAll() {
        return ResponseEntity.ok(deviceService.getAll());
    }

    @PostMapping
    public ResponseEntity<?> addNew(@RequestBody DeviceDTO deviceDTO) {
        try {
            return ResponseEntity.ok(deviceService.addDevice(deviceDTO));
        } catch(SQLConflict exception) {
            System.err.println("Exception [code: " + exception.getErrorCode() + "] " + exception.getMessage());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}
