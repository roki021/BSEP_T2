package com.hospitalplatform.hospital_platform.service;

import com.hospitalplatform.hospital_platform.dto.IntermediateToken;
import com.hospitalplatform.hospital_platform.dto.LoginDTO;
import com.hospitalplatform.hospital_platform.dto.UserTokenStateDTO;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public interface AuthService {
    IntermediateToken loginUser(LoginDTO credentials) throws UnsupportedEncodingException, NoSuchAlgorithmException;
}
