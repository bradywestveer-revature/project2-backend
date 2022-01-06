package com.revature.project2backend.repositories;

import com.revature.project2backend.models.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetRepo extends JpaRepository<PasswordReset, Integer> {
    PasswordReset findByUserId(Integer userId);
}
