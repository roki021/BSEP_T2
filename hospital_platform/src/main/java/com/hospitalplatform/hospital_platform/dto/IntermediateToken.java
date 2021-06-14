package com.hospitalplatform.hospital_platform.dto;

public class IntermediateToken {
    private UserTokenStateDTO userTokenStateDTO;
    private String fingerprint;

    public IntermediateToken(UserTokenStateDTO userTokenStateDTO, String fingerprintHash) {
        this.userTokenStateDTO = userTokenStateDTO;
        this.fingerprint = fingerprintHash;
    }

    public UserTokenStateDTO getUserTokenStateDTO() {
        return userTokenStateDTO;
    }

    public String getFingerprint() {
        return fingerprint;
    }
}
