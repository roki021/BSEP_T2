package com.hospitalplatform.hospital_platform.mapper;

import com.hospitalplatform.hospital_platform.dto.HospitalUserDTO;
import com.hospitalplatform.hospital_platform.models.HospitalUser;

public class HospitalUserMapper {
    public HospitalUserDTO getDTO(HospitalUser hospitalUser) {
        String role = "";

        if (hospitalUser.getRoles().stream().filter(r -> r.getName().equals("ROLE_ADMIN")).count() > 0)
            role = "admin";
        else if (hospitalUser.getRoles().stream().filter(r -> r.getName().equals("ROLE_DOCTOR")).count() > 0)
            role = "doctor";

        return new HospitalUserDTO(
                hospitalUser.getId(),
                hospitalUser.getFirstName(),
                hospitalUser.getLastName(),
                hospitalUser.getEmail(),
                hospitalUser.getUsername(),
                role
        );
    }
}
