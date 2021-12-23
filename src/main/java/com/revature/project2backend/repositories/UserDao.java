package com.revature.project2backend.repositories;

import com.revature.project2backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository <User, Integer> {}
