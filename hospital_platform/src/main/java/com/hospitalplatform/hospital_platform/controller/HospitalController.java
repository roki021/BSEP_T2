package com.hospitalplatform.hospital_platform.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLException;

@RestController
@RequestMapping("/api")
public class HospitalController {
    @GetMapping("/info")
    @PreAuthorize("hasAnyRole('ROLE_DOCTOR')")
    public String getHospitalInfo() {
        return "Info";
    }

    @PostMapping("/receive")
    public ResponseEntity<?> receiveMessage(@RequestBody String message) {
        System.out.println(message);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestParam String message) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpStatus status = restTemplate.exchange("https://host.docker.internal:5000/receive",
                    HttpMethod.POST,
                    new HttpEntity<String>(message),
                    Void.class).getStatusCode();

            return new ResponseEntity<>(status);
        } catch (ResourceAccessException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
}
