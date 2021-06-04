package com.hospitalplatform.hospital_platform.service;

import com.hospitalplatform.hospital_platform.dto.HospitalUserDTO;
import com.hospitalplatform.hospital_platform.dto.NewMemberDTO;
import com.hospitalplatform.hospital_platform.dto.RoleUpdateDTO;
import com.hospitalplatform.hospital_platform.models.HospitalUser;

import java.util.List;

public interface  HospitalUserService {
    HospitalUser getUser(String username);
    void createUser(NewMemberDTO member) throws Exception;
    void deleteUser(Integer id);
    void changeUserRole(Integer userId, RoleUpdateDTO roleUpdateDTO);
    List<HospitalUserDTO> getHospitalUsers();
}
