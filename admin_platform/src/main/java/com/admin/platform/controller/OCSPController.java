package com.admin.platform.controller;

import com.admin.platform.service.OCSPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/ocsp")
public class OCSPController {

    @Autowired
    private OCSPService ocspService;

    @PostMapping
    public ResponseEntity<byte[]> checkIfCertificateIsRevoked(@RequestBody byte[] request) throws Exception {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "application/ocsp-response");
        try {
            byte[] encodedResp = ocspService.getEncodedOCSPResponse(request);

            responseHeaders.set("Content-Length", encodedResp.length + "");
            return new ResponseEntity<>(encodedResp, responseHeaders, HttpStatus.OK);
        } catch(IOException exception) {
            return new ResponseEntity<>(responseHeaders, HttpStatus.BAD_REQUEST);
        }
    }
}
