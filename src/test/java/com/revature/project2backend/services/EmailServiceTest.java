package com.revature.project2backend.services;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;

class EmailServiceTest {
    JavaMailSender javaMailSender = Mockito.mock(JavaMailSender.class);
    private final EmailService emailService;
    public EmailServiceTest() {
        emailService = new EmailService(javaMailSender);
    }

    @Test
    void sendEmail() {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        /**
         * See PasswordResetServiceTest::sendPasswordResetEmail for where SimpleMailMessage is populated for better test
         */
        emailService.sendEmail(simpleMailMessage);
        Mockito.verify(this.javaMailSender, Mockito.times(1)).send (simpleMailMessage);
    }
}