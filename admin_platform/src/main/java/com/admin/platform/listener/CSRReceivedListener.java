package com.admin.platform.listener;

import com.admin.platform.event.OnCSREvent;
import com.admin.platform.model.CertificateSigningRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class CSRReceivedListener implements ApplicationListener<OnCSREvent> {
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnCSREvent onCSREvent) {
        SimpleMailMessage message = new SimpleMailMessage();
        // message.setFrom("noreply@baeldung.com");
        CertificateSigningRequest req = onCSREvent.getRequest();
        message.setTo(req.getEmail());
        message.setSubject("Confirm CSR");
        String messageTemplate =
                "<a target=\"_blank\" href=\"http://localhost:8080/api/certificate-signing-requests/confirm/%d\">Confirm</a>";
        message.setText(String.format(messageTemplate, req.getId()));
        try {
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
