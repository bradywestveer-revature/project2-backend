package com.revature.project2backend.services;

import com.revature.project2backend.exceptions.InvalidCredentialsException;
import com.revature.project2backend.exceptions.NotFoundException;
import com.revature.project2backend.models.User;
import com.revature.project2backend.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * The UserService class contains all methods necessary for creating, updating, retrieving, and logging in a User.
 * This class is also responsible for making sure sensitive data (like passwords) are encrypted.
 */
@Service
@Transactional
public class UserService {

	/**
	 * An instance of the UserRepository
	 */
	private final UserRepo userRepo;


	/**
	 * A password encoder
	 */
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder ();

	/**
	 * The constructor for UserService that uses dependency injection through Spring.
	 *
	 * @param userRepo An instance of the UserRepository used for gaining access to methods in the JPA
	 */
	@Autowired
	public UserService (UserRepo userRepo) {
		this.userRepo = userRepo;
	}

	/**
	 * The argument takes in a User Object, encrypts the password field, and saves that Object to a database.
	 *
	 * @param user A User Object that is saved to the database
	 */
	public void createUser (User user) {
		user.setPassword (passwordEncoder.encode (user.getPassword ()));
		
		this.userRepo.save (user);
	}

	/**
	 * Returns a List of User Objects.
	 *
	 * @return A List of User Objects
	 */
	public List <User> getUsers () {
		return this.userRepo.findAll ();
	}

	/**
	 * Returns a User Object with a specific id. Throws a NotFoundException if no User is found.
	 *
	 * @param id An Integer associated with a specific User Object
	 * @return A User Object with the same id
	 * @throws NotFoundException Throws when a user is not found with the given id
	 */
	public User getUser (Integer id) throws NotFoundException {
		User user = this.userRepo.findById (id).orElse (null);
		
		if (user == null) {
			throw new NotFoundException ("User with id: " + id + " not found");
		}
		
		return user;
	}

	/**
	 * Returns a User Object with a specific username.
	 *
	 * @param username A unique String associated with a specific User Object
	 * @return A User Object with same username
	 */
	public User getUserByUsername (String username) {
		return this.userRepo.findByUsername (username);
	}

	/**
	 * Returns a User Object with a specific email.
	 *
	 * @param email A unique email associated with a specific User Object
	 * @return A User Object with same email
	 */
	public User getUserByEmail (String email) {
		return this.userRepo.findByEmail (email);
	}

	/**
	 * Updates an older User Object with the User Object that is passed in and encrypts their password.
	 *
	 * @param user A User Object that is going to be updated.
	 * @throws NotFoundException Thrown when no User Object is found
	 */
	public void updateUser (User user) throws NotFoundException {
		//todo replace this with boolean encryptPassword paramater?
		//if user's password has been updated
		if (!user.getPassword ().equals (this.getUser (user.getId ()).getPassword ())) {
			//encrypt the new password
			user.setPassword (passwordEncoder.encode (user.getPassword ()));
		}
		
		this.userRepo.save (user);
	}

	/**
	 * Returns a User Object from the database.
	 * The argument identifier can be either an email or username associated with the User Object.
	 * The password argument is encoded and then checked in the database.
	 * If no User is found, throws an InvalidCredentialsException.
	 *
	 * @param identifier A String with a username or email
	 * @param password A String with a password
	 * @return A User Object
	 * @throws InvalidCredentialsException Thrown when no user is found with the identifier
	 */
	public User loginUser (String identifier, String password) throws InvalidCredentialsException {
		//todo use service or repo methods?
		User user = this.getUserByUsername (identifier);
		
		//if no user was found with username
		if (user == null) {
			user = this.getUserByEmail (identifier);
		}
		
		//if no user was found with username or email
		if (user == null) {
			throw new InvalidCredentialsException ();
		}

		if (passwordEncoder.matches (password, user.getPassword ())) {
			return user;
		}
		
		else {
			throw new InvalidCredentialsException ();
		}
	}

	/**
	 * Returns a User Object by checking if it's assigned a token and if that token matches.
	 *
	 * @param token A token generated for security
	 * @return A User Object associated with the token
	 */
	public User getUserByPasswordResetToken(String token) {
		return userRepo.findByPasswordResetToken(token);
	}

	/**
	 * Used for encrypting a password after a password reset.
	 *
	 * @param user A User Object ot be updated
	 * @param password The new password to be encoded
	 */
	public void updateUserAlwaysEncrypt (User user, String password) {

		//todo don't use, use other updateUser method, there is a problem with encryption
		
		// Assumes valid user already populated
		user.setPassword (passwordEncoder.encode (password));
		this.userRepo.save (user);
	}
}
