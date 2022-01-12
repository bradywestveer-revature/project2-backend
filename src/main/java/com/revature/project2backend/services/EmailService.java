package com.revature.project2backend.services;

import com.revature.project2backend.exceptions.InvalidValueException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * This class contains all methods associated with sending an email with a link to reset a password.
 */
@Service
public class EmailService {

	/**
	 * An instance of the JavaMailSender
	 */
	private final JavaMailSender javaMailSender;

	/**
	 * A logger to keep track of errors involved in the EmailService
	 */
	private static final Logger logger = Logger.getLogger(EmailService.class);

	/**
	 * The constructor for the EmailService that uses dependency injection through Spring.
	 *
	 * @param javaMailSender An instance of the JavaMailSender
	 */
	@Autowired
	public EmailService (JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	/**
	 * Instantiates JavaMailSender
	 *
	 * @return An instantiated JavaMailSender Object
	 */
	@Autowired
	public JavaMailSender getJavaMailSender () {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl ();
		Properties props = mailSender.getJavaMailProperties ();
		props.put ("mail.debug", "true");
		return mailSender;
	}

	/**
	 * Sends an email to a specific user. The argument is formatted through the javaMailSender instance.
	 *
	 * @param simpleMailMessage A SimpleMailMessage Object
	 * @throws InvalidValueException
	 */
	@Async
	public void sendEmail (SimpleMailMessage simpleMailMessage) throws InvalidValueException {
		try {
			javaMailSender.send(simpleMailMessage);
		} catch (MailSendException e) {
			if (e.getMessage().contains("554 Message rejected: Email address is not verified.")) {
				logger.error("AWS SES is in sandbox mode, e-mail recipient "+ simpleMailMessage.getTo()[0]+ " is either valid and not registered or not a valid e-mail address.");
			} else {
				logger.error(e);
			}
			throw new InvalidValueException("Password reset e-mail NOT sent, please check if the e-mail address is valid and registered.");
		}
	}
}