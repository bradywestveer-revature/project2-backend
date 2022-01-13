package com.revature.project2backend.services;

import com.revature.project2backend.exceptions.InvalidValueException;
import com.revature.project2backend.exceptions.NotFoundException;
import com.revature.project2backend.models.PasswordReset;
import com.revature.project2backend.models.User;
import com.revature.project2backend.repositories.PasswordResetRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * The PasswordResetService is responsible for all methods involved in token generation and deletion as well as password
 * reset services by email.
 */
@Service
public class PasswordResetService {

	/**
	 * A variable used to generate a link in an email leading to the change-password page with a valid token.
	 */
	private final String RESET_URL = System.getenv ("PROJECT2_FRONTEND_URL") + "change-password";

	/**
	 * An instance of the PasswordResetRepository
	 */
	private final PasswordResetRepo passwordResetRepo;

	/**
	 * An instance of the EmailService for access to methods
	 */
	private final EmailService emailService;

	/**
	 * An instance of the UserService for access to methods
	 */
	private final UserService userService;

	/**
	 * The constructor for the PostImageService that uses dependency injection through Spring.
	 *
	 * @param passwordResetRepo An instance of the PasswordResetRepository
	 * @param emailService An instance of the EmailService
	 * @param userService An instance of the UserService
	 */
	@Autowired
	public PasswordResetService (PasswordResetRepo passwordResetRepo, EmailService emailService, UserService userService) {
		this.passwordResetRepo = passwordResetRepo;
		this.emailService = emailService;
		this.userService = userService;
	}

	/**
	 * Returns a String generated token used for validation.
	 *
	 * @return String
	 */
	private String generateToken () {
		return UUID.randomUUID ().toString ();
	}

	/**
	 * This method constructs a PasswordReset Object and assigns or updates the token associated with User Object that is passed
	 * in the argument.
	 *
	 * @param user A User Object
	 * @return A PasswordReset Object
	 */
	public PasswordReset createOrUpdateToken (User user) {
		PasswordReset passwordReset = passwordResetRepo.findByUserId (user.getId ());

		if (passwordReset == null) {
			passwordReset = new PasswordReset (null, user, this.generateToken ());
		} else {
			passwordReset.setToken (this.generateToken ());
		}
		passwordResetRepo.save (passwordReset);
		return passwordReset;
	}

	/**
	 * This method will email the user with a link to the change-password page. A token will be included in the
	 * link.
	 *
	 * @param user A User Object
	 * @param passwordReset A PasswordReset Object
	 * @throws InvalidValueException Thrown when the user is not found or token is invalid
	 */
	public void sendPasswordResetEmail (User user, PasswordReset passwordReset) throws InvalidValueException {
		SimpleMailMessage passwordResetEmail = new SimpleMailMessage ();
		passwordResetEmail.setFrom ("jason.chan@revature.net");
		passwordResetEmail.setTo (user.getEmail ());
		passwordResetEmail.setSubject ("Password Reset Requested for Team Fire Social Media App");
		passwordResetEmail.setText ("Hi " + user.getFirstName () + " " + user.getLastName () + ":\n\n" + "Your password has been reset from the Team Fire Social Media App (aka Project 2)\n\n" + "Please click the link below to change your password:\n" + this.RESET_URL + "?token=" + passwordReset.getToken ());
		emailService.sendEmail (passwordResetEmail);
	}

	/**
	 * Returns a User Object associated with the specific argument token that is given.
	 * @param token A randomly generated String
	 * @return A User Object
	 */
	public User getUserByPasswordResetToken (String token) {
		if (token==null || token.length()<36) return null;

		return userService.getUserByPasswordResetToken (token);
	}

	/**
	 * This method updates and saves the user arguments password and deletes the token associated with this user.
	 *
	 * @param user A User Object
	 * @param password A String that is going to be set to the users password
	 */
	public void changePasswordAndDeleteToken (User user, String password) {
		userService.updateUserAlwaysEncrypt (user, password);
		passwordResetRepo.deleteById (user.getPasswordReset ().getId ());
	}

}
