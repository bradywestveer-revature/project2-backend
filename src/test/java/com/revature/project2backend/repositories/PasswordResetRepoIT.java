package com.revature.project2backend.repositories;

import com.revature.project2backend.models.PasswordReset;
import com.revature.project2backend.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
class PasswordResetRepoIT {

    String token = null;
    
    @Autowired
    PasswordResetRepo passwordResetRepo;

    @Autowired
    UserRepo userRepo;

    @BeforeEach
    void setUp() {
        User user = new User(null, "Jay", "Jones", "jayjones@javadev.com", "jayjones", "", "", null);
        User userWithId = userRepo.save(user);
        this.token = UUID.randomUUID ().toString ();
        PasswordReset passwordReset = new PasswordReset(null, userWithId, this.token);
        passwordResetRepo.save(passwordReset);
    }

    @AfterEach
    void tearDown() {
        passwordResetRepo.deleteAll();
    }

    @Test
    void findByUserId() {
        User user = userRepo.findByUsername("jayjones");
        PasswordReset passwordReset = passwordResetRepo.findByUserId(user.getId());
        assertEquals(this.token, passwordReset.getToken());
    }

    @Test
    void findByUserId_NoUserFound() {
        User user = userRepo.findByUsername("jayjones");
        PasswordReset passwordReset = passwordResetRepo.findByUserId(user.getId()+1);
        assertNull(passwordReset);
    }
}