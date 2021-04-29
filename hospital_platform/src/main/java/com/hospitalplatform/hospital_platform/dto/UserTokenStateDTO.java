package com.hospitalplatform.hospital_platform.dto;

public class UserTokenStateDTO {
    private String accessToken;
    private int expiredIn;

    public UserTokenStateDTO(String accessToken, int expiredIn) {
        this.accessToken = accessToken;
        this.expiredIn = expiredIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public int getExpiredIn() {
        return expiredIn;
    }
}
