package com.hospitalplatform.hospital_platform.mapper;

import com.hospitalplatform.hospital_platform.dto.HospitalUserDTO;
import com.hospitalplatform.hospital_platform.models.HospitalAdmin;
import com.hospitalplatform.hospital_platform.models.HospitalDoctor;
import com.hospitalplatform.hospital_platform.models.HospitalUser;

public class HospitalUserMapper {
    public HospitalUserDTO getDTO(HospitalUser hospitalUser) {
        String role = "";

        if (hospitalUser instanceof HospitalAdmin)
            role = "admin";
        else if (hospitalUser instanceof HospitalDoctor)
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
