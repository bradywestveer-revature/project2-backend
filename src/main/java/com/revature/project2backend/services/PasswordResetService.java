package com.revature.project2backend.services;

import com.revature.project2backend.exceptions.NotFoundException;
import com.revature.project2backend.models.PasswordReset;
import com.revature.project2backend.models.User;
import com.revature.project2backend.repositories.PasswordResetRepo;
import com.revature.project2backend.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {
    private final String RESET_URL = "http://localhost:4200/change-password";
    private final PasswordResetRepo passwordResetRepo;
    private final EmailService emailService;
    private final UserService userService;

    @Autowired
    public PasswordResetService(PasswordResetRepo passwordResetRepo, EmailService emailService, UserService userService) {
        this.passwordResetRepo = passwordResetRepo;
        this.emailService = emailService;
        this.userService = userService;
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    public PasswordReset createOrUpdateToken(User user){
        PasswordReset passwordReset = passwordResetRepo.findByUserId(user.getId());
        // Once the token is used we just delete it later on change of password, if the password reset record exists
        // already its because they didn't use one or more of their reset tokens
        if (passwordReset==null) {
            passwordReset = new PasswordReset(null, user, this.generateToken());
        } else {
            passwordReset.setToken(this.generateToken());
        }
        passwordResetRepo.save(passwordReset);
        Integer id = passwordReset.getId();
        Optional<PasswordReset> passwordReset2 = passwordResetRepo.findById(id);
        return passwordReset;
    }

    public void sendPasswordResetEmail(User user, PasswordReset passwordReset){
        SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
        passwordResetEmail.setFrom("jason.chan@revature.net");
        passwordResetEmail.setTo(user.getEmail());
        passwordResetEmail.setSubject("Password Reset Requested for Team Fire Social Media App");
        passwordResetEmail.setText("Hi "+ user.getFirstName()+ " " +user.getLastName() + ":\n\n"+
                "Your password has been reset from the Team Fire Social Media App (aka Project 2)\n\n"+
                "Please click the link below to change your password:\n" +
                        this.RESET_URL + "?token=" + passwordReset.getToken());
        emailService.sendEmail(passwordResetEmail);
    }

    public User getUserByPasswordResetToken(String token) {
        User user = userService.getUserByPasswordResetToken(token);
        return user;
    }

    public void changePasswordAndDeleteToken(User user, String password) throws NotFoundException {
        userService.updateUserAlwaysEncrypt(user, password);
        passwordResetRepo.deleteById(user.getPasswordReset().getId());
    }
}
