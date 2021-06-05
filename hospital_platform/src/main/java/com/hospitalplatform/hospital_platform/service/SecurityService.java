package com.hospitalplatform.hospital_platform.service;

import com.hospitalplatform.hospital_platform.dto.SecretCommunicationTokenDTO;

public interface SecurityService {
    SecretCommunicationTokenDTO getSecurityToken();
    void setSecurityToken(SecretCommunicationTokenDTO token);
    boolean checkSecurityToken(String token);
    void changeSecurityTokenStatus(boolean tokenActive) throws Exception;
}
