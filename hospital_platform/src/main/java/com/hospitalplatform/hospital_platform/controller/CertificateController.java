package com.hospitalplatform.hospital_platform.controller;

import com.hospitalplatform.hospital_platform.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("api/certificate")
public class CertificateController {
    @Autowired
    private CertificateService certificateService;
    //TODO: jel ovo sluzi za ista?
    @PostMapping
    public ResponseEntity<String> receiveCertificate(@RequestBody byte[] request) throws IOException {
        System.out.println(new String(request));
        certificateService.installCertificate(new String(request));


        return new ResponseEntity<>(HttpStatus.OK);
    }
}
