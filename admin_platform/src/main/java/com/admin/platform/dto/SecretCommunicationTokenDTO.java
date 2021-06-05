package com.admin.platform.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class SecretCommunicationTokenDTO {
    private String token;

    public SecretCommunicationTokenDTO(String token) {
        this.token = token;
    }
}
