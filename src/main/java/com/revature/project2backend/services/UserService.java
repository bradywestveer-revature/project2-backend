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

@Service
@Transactional
public class UserService {
	private final UserRepo userRepo;
	
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder ();
	
	@Autowired
	public UserService (UserRepo userRepo) {
		this.userRepo = userRepo;
	}
	
	public void createUser (User user) {
		user.setPassword (passwordEncoder.encode (user.getPassword ()));
		
		this.userRepo.save (user);
	}
	
	public List <User> getUsers () {
		return this.userRepo.findAll ();
	}
	
	public User getUser (Integer id) throws NotFoundException {
		User user = this.userRepo.findById (id).orElse (null);
		
		if (user == null) {
			throw new NotFoundException ("User with id: " + id + " not found");
		}
		
		return user;
	}
	
	public User getUserByUsername (String username) {
		return this.userRepo.findByUsername (username);
	}
	
	public User getUserByEmail (String email) {
		return this.userRepo.findByEmail (email);
	}

	public User getUserByPasswordResetId (Integer passwordResetId) { return this.userRepo.findByPasswordResetId(passwordResetId); }
	
	public void updateUser (User user) throws NotFoundException {
		//todo replace this with boolean encryptPassword paramater?
		//if user's password has been updated
		if (!user.getPassword ().equals (this.getUser (user.getId ()).getPassword ())) {
			//encrypt the new password
			user.setPassword (passwordEncoder.encode (user.getPassword ()));
		}
		
		this.userRepo.save (user);
	}
	
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

	public User getUserByPasswordResetToken(String token) {
		return userRepo.findByPasswordResetToken(token);
	}

	public void updateUserAlwaysEncrypt (User user, String password) throws NotFoundException {
		// Assumes valid user already populated
		user.setPassword (passwordEncoder.encode (password));
		this.userRepo.save (user);
	}
}
