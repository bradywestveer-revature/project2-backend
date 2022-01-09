package com.revature.project2backend.services;

import com.revature.project2backend.exceptions.InvalidValueException;
import com.sun.mail.smtp.SMTPSendFailedException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService {

	private final JavaMailSender javaMailSender;
	private static Logger logger = Logger.getLogger(EmailService.class);
	
	@Autowired
	public EmailService (JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}
	
	@Autowired
	public JavaMailSender getJavaMailSender () {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl ();
		Properties props = mailSender.getJavaMailProperties ();
		props.put ("mail.debug", "true");
		return mailSender;
	}
	
	@Async
	public void sendEmail (SimpleMailMessage simpleMailMessage) throws InvalidValueException {
		try {
			javaMailSender.send(simpleMailMessage);
		} catch (MailSendException e) {
			if (e.getMessage().contains("554 Message rejected: Email address is not verified.")) {
				logger.error("AWS SES is in sandbox mode, e-mail recipient"+ simpleMailMessage.getTo()+ " is either valid and not registered or not a valid e-mail address.");
			} else {
				logger.error(e);
			}
			throw new InvalidValueException("Password reset e-mail NOT sent, please check if the e-mail address is valid and registered.");
		}
	}
}