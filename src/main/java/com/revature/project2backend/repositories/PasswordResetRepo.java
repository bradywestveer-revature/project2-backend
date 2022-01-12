package com.revature.project2backend.repositories;

import com.revature.project2backend.models.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetRepo extends JpaRepository<PasswordReset, Integer> {

    /**
     * Finds a User Object by its id and returns that user in a PasswordReset Object.
     *
     * @param userId An Integer associated with a specific User Object
     * @return A PasswordReset Object
     */
    PasswordReset findByUserId(Integer userId);
}
