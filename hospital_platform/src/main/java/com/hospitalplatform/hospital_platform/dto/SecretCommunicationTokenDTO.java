package com.hospitalplatform.hospital_platform.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class SecretCommunicationTokenDTO {
    private String token;
    private boolean active;

    public SecretCommunicationTokenDTO() {
        this("", false);
    }

    public SecretCommunicationTokenDTO(String token, boolean active) {
        this.token = token;
        this.active = active;
    }

    public String getToken() {
        return token;
    }

    public boolean isActive() {
        return active;
    }
}
