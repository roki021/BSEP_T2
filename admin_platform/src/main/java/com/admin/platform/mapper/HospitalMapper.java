package com.admin.platform.mapper;

import com.admin.platform.dto.HospitalDTO;
import com.admin.platform.model.Hospital;

public class HospitalMapper {
    public HospitalDTO getDTO(Hospital hospital) {
        return new HospitalDTO(hospital.getId(), hospital.getOrganization(), hospital.getOrganizationUnit());
    }
}
