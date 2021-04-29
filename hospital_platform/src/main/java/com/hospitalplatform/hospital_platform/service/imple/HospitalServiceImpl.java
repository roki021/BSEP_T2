package com.hospitalplatform.hospital_platform.service.imple;

import com.hospitalplatform.hospital_platform.dto.HospitalConfigurationDTO;
import com.hospitalplatform.hospital_platform.service.HospitalService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HospitalServiceImpl implements HospitalService {
    @Value("${hospital.organization}")
    private String organization;

    @Value("${hospital.organization_unit}")
    private String organizationUnit;

    @Value("${hospital.country}")
    private String country;

    @Override
    public HospitalConfigurationDTO getConfiguration() {
        return new HospitalConfigurationDTO(
                organization,
                organizationUnit,
                country
        );
    }
}
