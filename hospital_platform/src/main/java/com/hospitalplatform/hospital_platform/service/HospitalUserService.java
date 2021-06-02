package com.hospitalplatform.hospital_platform.service;

import com.hospitalplatform.hospital_platform.dto.HospitalUserDTO;
import com.hospitalplatform.hospital_platform.dto.NewMemberDTO;
import com.hospitalplatform.hospital_platform.models.HospitalUser;

import java.util.List;

public interface  HospitalUserService {
    HospitalUser getUser(String username);
    void createUser(NewMemberDTO member) throws Exception;
    List<HospitalUserDTO> getHospitalUsers();
}
