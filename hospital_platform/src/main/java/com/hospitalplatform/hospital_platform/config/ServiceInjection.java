package com.hospitalplatform.hospital_platform.config;

import com.hospitalplatform.hospital_platform.service.CertificateService;
import com.hospitalplatform.hospital_platform.service.imple.CertificateServiceImpl;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceInjection {
    CertificateService getCertificateServiceInstance() {
        return new CertificateServiceImpl();
    }
}
