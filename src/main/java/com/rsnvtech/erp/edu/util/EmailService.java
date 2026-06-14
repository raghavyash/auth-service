package com.rsnvtech.erp.edu.util;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;

public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async // Runs on a background thread pool
    public void sendVerificationEmail(String to, String token) {
        String verificationUrl = "http://localhost:8080/api/v1/auth/verify-email?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Complete Your Registration");
        message.setText("Please click the link below to verify your email address:\n" + verificationUrl);

        mailSender.send(message);
    }
}
