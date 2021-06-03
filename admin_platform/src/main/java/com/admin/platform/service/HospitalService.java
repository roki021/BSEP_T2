package com.admin.platform.service;

import com.admin.platform.dto.HospitalDTO;
import com.admin.platform.dto.HospitalUserDTO;
import com.admin.platform.dto.NewMemberDTO;

import java.util.List;

public interface HospitalService {
    List<HospitalDTO> getHospitals();
    List<HospitalUserDTO> getHospitalMembers(Integer hospitalId) throws Exception;

    void addHospitalMember(Integer hospitalId, NewMemberDTO member) throws Exception;

    void deleteHospitalMember(Integer hospitalId, Integer memberId) throws Exception;

    void changeHospitalMemberRole(Integer hospitalId, Integer memberId, Object newRole);
    // void saveHospitalMemberPermissions(Integer hospitalId, Integer memberId, Object newPermissions);
}
