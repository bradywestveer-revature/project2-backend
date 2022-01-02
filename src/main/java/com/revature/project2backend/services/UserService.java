package com.revature.project2backend.services;

import com.revature.project2backend.exceptions.InvalidCredentialsException;
import com.revature.project2backend.exceptions.NotFoundException;
import com.revature.project2backend.models.User;
import com.revature.project2backend.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
	private final UserRepo userRepo;
	
	@Autowired
	public UserService (UserRepo userRepo) {
		this.userRepo = userRepo;
	}
	
	public void createUser (User user) {
		this.userRepo.save (user);
	}
	
	public List <User> getUsers () {
		return this.userRepo.findAll ();
	}
	
	public User getUser (Integer id) throws NotFoundException {
		User user = this.userRepo.findById (id).orElse (null);
		
		if (user == null) {
			throw new NotFoundException ("User with id: " + id.toString () + " not found");
		}
	}
	
	public User getUserByUsername (String username) {
		return this.userRepo.findByUsername (username);
	}
	
	public User getUserByEmail (String email) {
		return this.userRepo.findByEmail (email);
	}
	
	public void updateUser (User user) {
		this.userRepo.save (user);
	}
	
	public User loginUser (String identifier, String password) throws InvalidCredentialsException {
		User user = this.userRepo.findByUsername (identifier);
		
		//if no user was found with username
		if (user == null) {
			user = this.userRepo.findByEmail (identifier);
		}
		
		//if no user was found with username or email
		if (user == null) {
			throw new InvalidCredentialsException ();
		}
		
		if (user.getPassword ().equals (password)) {
			return user;
		}
		
		else {
			throw new InvalidCredentialsException ();
		}
	}
}
