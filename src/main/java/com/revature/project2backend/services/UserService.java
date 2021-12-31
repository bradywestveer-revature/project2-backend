package com.revature.project2backend.services;

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
	
	public User getUser (Integer id) {
		return this.userRepo.findById (id).orElse (null);
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
}
