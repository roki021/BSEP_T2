package com.hospitalplatform.hospital_platform.models;

import com.hospitalplatform.hospital_platform.dto.SecretCommunicationTokenDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SecretCommunicationToken {
    @Id
    private Integer id;

    @Column
    private String token;

    @Column
    private boolean active;

    public SecretCommunicationToken() {
        this(null);
    }

    public SecretCommunicationToken(String token) {
        this.id = 0;
        this.token = token;
        this.active = false;
    }

    public String getToken() {
        return token;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
