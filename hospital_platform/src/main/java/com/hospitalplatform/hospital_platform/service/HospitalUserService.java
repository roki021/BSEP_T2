package com.hospitalplatform.hospital_platform.service;

import com.hospitalplatform.hospital_platform.models.HospitalUser;

public interface HospitalUserService {
    HospitalUser getUser(String username);
}
