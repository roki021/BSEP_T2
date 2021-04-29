package com.admin.platform.event;

import com.admin.platform.model.CertificateSigningRequest;
import org.springframework.context.ApplicationEvent;

public class OnCSREvent extends ApplicationEvent {
    private CertificateSigningRequest request;

    public OnCSREvent(CertificateSigningRequest request) {
        super(request);
        this.request = request;
    }

    public CertificateSigningRequest getRequest() {
        return request;
    }
}
