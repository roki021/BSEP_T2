package com.hospitalplatform.hospital_platform.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HospitalController {
    @GetMapping("/info")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String getHospitalInfo() {
        return "Info";
    }
}
