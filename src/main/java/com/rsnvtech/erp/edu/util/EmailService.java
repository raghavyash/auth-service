package com.rsnvtech.erp.edu.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private  JavaMailSender javaMailSender;


    @Async // Runs on a background thread pool
    public void sendVerificationEmail(String to, String token) {
        String verificationUrl = "http://localhost:8080/api/v1/auth/verify-email?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Complete Your Registration");
        message.setText("Please click the link below to verify your email address:\n" + verificationUrl);

        javaMailSender.send(message);
    }
}
