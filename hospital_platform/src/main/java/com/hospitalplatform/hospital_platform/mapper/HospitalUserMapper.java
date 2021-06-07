package com.hospitalplatform.hospital_platform.mapper;

import com.hospitalplatform.hospital_platform.dto.HospitalUserDTO;
import com.hospitalplatform.hospital_platform.models.HospitalUser;
import com.hospitalplatform.hospital_platform.privileges.Privilege;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HospitalUserMapper {
    public HospitalUserDTO getDTO(HospitalUser hospitalUser) {
        String role = "";

        if (hospitalUser.getRoles().stream().filter(r -> r.getName().equals("ROLE_ADMIN")).count() > 0)
            role = "admin";
        else if (hospitalUser.getRoles().stream().filter(r -> r.getName().equals("ROLE_DOCTOR")).count() > 0)
            role = "doctor";

        List<Privilege> privilegeList =
                hospitalUser.getPrivileges().stream().map(
                        privilege -> Privilege.valueOf(privilege.getName())).collect(Collectors.toList());

        return new HospitalUserDTO(
                hospitalUser.getId(),
                hospitalUser.getFirstName(),
                hospitalUser.getLastName(),
                hospitalUser.getEmail(),
                hospitalUser.getUsername(),
                role,
                privilegeList
        );
    }
}
