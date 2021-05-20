package com.hospitalplatform.hospital_platform.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/certificate")
public class CertificateController {

    @PostMapping
    public ResponseEntity<String> receiveCertificate(@RequestBody byte[] request) {
        System.out.println(new String(request));

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
