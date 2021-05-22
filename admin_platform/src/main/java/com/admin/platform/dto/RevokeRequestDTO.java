package com.admin.platform.dto;

import javax.validation.constraints.NotBlank;

public class RevokeRequestDTO {

    @NotBlank
    private String reason;

    private Long certId;

    public RevokeRequestDTO() {}

    public RevokeRequestDTO(String reason, Long certId) {
        this.reason = reason;
        this.certId = certId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getCertId() {
        return certId;
    }

    public void setCertId(Long certId) {
        this.certId = certId;
    }
}
