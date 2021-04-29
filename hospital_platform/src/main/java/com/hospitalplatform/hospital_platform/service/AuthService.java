package com.hospitalplatform.hospital_platform.service;

import com.hospitalplatform.hospital_platform.dto.LoginDTO;
import com.hospitalplatform.hospital_platform.dto.UserTokenStateDTO;

public interface AuthService {
    UserTokenStateDTO loginUser(LoginDTO credentials);
}
