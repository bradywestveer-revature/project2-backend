package com.revature.project2backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService {
	
	private final JavaMailSender javaMailSender;
	
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
	public void sendEmail (SimpleMailMessage simpleMailMessage) {
		javaMailSender.send (simpleMailMessage);
	}
}