package com.revature.project2backend.services;

import com.revature.project2backend.exceptions.InvalidValueException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;

class EmailServiceTest {
    final JavaMailSender javaMailSender = Mockito.mock(JavaMailSender.class);
    private final EmailService emailService;
    public EmailServiceTest() {
        emailService = new EmailService(javaMailSender);
    }

    @Test
    void sendEmail() throws InvalidValueException {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        /**
         * See PasswordResetServiceTest::sendPasswordResetEmail for where SimpleMailMessage is populated for better test
         */
        emailService.sendEmail(simpleMailMessage);
        Mockito.verify(this.javaMailSender, Mockito.times(1)).send (simpleMailMessage);
    }

    @Test
    void sendEmail_SMTPError() {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom ("jason.chan@revature.net");
        simpleMailMessage.setTo ("jsmith@javadev.com");
        simpleMailMessage.setSubject ("Password Reset Requested for Team Fire Social Media App");
        simpleMailMessage.setText ("Test e-mail body...");
        /**
         * When javaMailSender.send(...) encounters an error we get a MailException raised which in turn will cause
         * EmailService::sendEmail(...) to throw a InvalidValueException with user-friendly error message
         */
        Mockito.doThrow(new MailSendException("554 Message rejected: Email address is not verified.")).when(javaMailSender).send(simpleMailMessage);
        try {
            emailService.sendEmail(simpleMailMessage);
        } catch (InvalidValueException e) {
            assertTrue(e.getMessage().contains("Password reset e-mail NOT sent, please check if the e-mail address is valid and registered."));
        }
        Mockito.verify(this.javaMailSender, Mockito.times(1)).send (simpleMailMessage);
    }
}