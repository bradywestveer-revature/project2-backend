package com.revature.project2backend.services;

import com.revature.project2backend.exceptions.InvalidValueException;
import com.revature.project2backend.exceptions.NotFoundException;
import com.revature.project2backend.models.PasswordReset;
import com.revature.project2backend.models.User;
import com.revature.project2backend.repositories.PasswordResetRepo;
import com.revature.project2backend.repositories.UserRepo;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PasswordResetServiceTest {

    private final String RESET_URL = System.getenv ("PROJECT2_FRONTEND_URL") + "change-password";
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final PasswordResetRepo passwordResetRepo = Mockito.mock(PasswordResetRepo.class);
    final JavaMailSender javaMailSender = Mockito.mock(JavaMailSender.class);
    private final EmailService emailService;
    final UserRepo userRepo = Mockito.mock(UserRepo.class);
    private final UserService userService;
    final PasswordResetService passwordResetService;

    public PasswordResetServiceTest() {
        this.userService = new UserService(this.userRepo);
        this.emailService = new EmailService(this.javaMailSender);
        this.passwordResetService = new PasswordResetService(passwordResetRepo, emailService, userService);
    }
	
	@Test
    void createOrUpdateToken_FoundOldUnusedToken() {
        User user = new User(1, "John", "Smith", "johnsmith@javadev.com", "jsmith", passwordEncoder.encode("jsmith123"), "", null);
        String oldToken = UUID.randomUUID ().toString ();
        PasswordReset passwordReset = new PasswordReset(1, user, oldToken);
        user.setPasswordReset(passwordReset);
        Mockito.when(passwordResetRepo.findByUserId (user.getId ())).thenReturn(passwordReset);
        PasswordReset passwordResetNew = passwordResetService.createOrUpdateToken(user);
        /**
         * If there is an old token it means we didn't use it so this is to see the new token is set
        * */
        assertNotEquals(oldToken, passwordResetNew.getToken());
        Mockito.verify(this.passwordResetRepo, Mockito.times(1)).save(passwordReset);
    }

    @Test
    void createOrUpdateToken_NoOldTokenFound() {
        User user = new User(1, "John", "Smith", "johnsmith@javadev.com", "jsmith", passwordEncoder.encode("jsmith123"), "", null);
        Mockito.when(passwordResetRepo.findByUserId (user.getId ())).thenReturn(null);
        PasswordReset passwordResetNew = passwordResetService.createOrUpdateToken(user);
        /**
         * If there is NOT an existing token it means there was no passwordReset record, it was either deleted when the user
         * changed (reset) their password by email or never tried to reset their password via email before
         * */
		assertEquals (36, passwordResetNew.getToken ().length ());
        PasswordReset passwordResetTest = new PasswordReset(null, user, passwordResetNew.getToken());
        Mockito.verify(this.passwordResetRepo, Mockito.times(1)).save(passwordResetTest);
    }

    @Test
    void sendPasswordResetEmail() throws InvalidValueException {
        User user = new User(1, "John", "Smith", "johnsmith@javadev.com", "jsmith", passwordEncoder.encode("jsmith123"), "", null);
        String oldToken = UUID.randomUUID ().toString ();
        PasswordReset passwordReset = new PasswordReset(1, user, oldToken);
        user.setPasswordReset(passwordReset);
        Mockito.when(passwordResetRepo.findByUserId (user.getId ())).thenReturn(passwordReset);
        PasswordReset passwordResetNew = passwordResetService.createOrUpdateToken(user);
        /**
         * Note that EmailService is better tested from following method, the other specific test is just
         * making sure javaMailSender (mocked) is called once as shown
        * */
        passwordResetService.sendPasswordResetEmail(user, passwordResetNew);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage ();
        simpleMailMessage.setFrom ("jason.chan@revature.net");
        simpleMailMessage.setTo (user.getEmail ());
        simpleMailMessage.setSubject ("Password Reset Requested for Team Fire Social Media App");
        simpleMailMessage.setText ("Hi " + user.getFirstName () + " " + user.getLastName () + ":\n\n" + "Your password has been reset from the Team Fire Social Media App (aka Project 2)\n\n" + "Please click the link below to change your password:\n" + this.RESET_URL + "?token=" + passwordResetNew.getToken ());
        Mockito.verify(this.javaMailSender, Mockito.times(1)).send (simpleMailMessage);
    }

    @Test
    void getUserByPasswordResetToken_ValidToken() {
        User expectedResult = new User(1, "John", "Smith", "johnsmith@javadev.com", "jsmith", passwordEncoder.encode("jsmith123"), "", null);
        String token = UUID.randomUUID ().toString ();
        Mockito.when(userRepo.findByPasswordResetToken(token)).thenReturn(expectedResult);
        User actualResult = passwordResetService.getUserByPasswordResetToken(token);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void getUserByPasswordResetToken_InValidTokenLength() {
        User expectedResult = new User(1, "John", "Smith", "johnsmith@javadev.com", "jsmith", passwordEncoder.encode("jsmith123"), "", null);
        String token = UUID.randomUUID ().toString ();
        /**
         * Make the token an invalid length
        * */
        String invalidToken = token.substring(0,34);
        Mockito.when(userRepo.findByPasswordResetToken(token)).thenReturn(expectedResult);
        User actualResult = passwordResetService.getUserByPasswordResetToken(invalidToken);
        assertNotEquals(expectedResult, actualResult);
    }

    @Test
    void getUserByPasswordResetToken_NullToken() {
        User expectedResult = new User(1, "John", "Smith", "johnsmith@javadev.com", "jsmith", passwordEncoder.encode("jsmith123"), "", null);
        String token = UUID.randomUUID ().toString ();
        /**
         * Make the token null
         * */
        String invalidToken = null;
        Mockito.when(userRepo.findByPasswordResetToken(token)).thenReturn(expectedResult);
        User actualResult = passwordResetService.getUserByPasswordResetToken(invalidToken);
        assertNull(actualResult);
    }

    @Test
    void changePasswordAndDeleteToken() throws NotFoundException {
        User user = new User(1, "John", "Smith", "johnsmith@javadev.com", "jsmith", passwordEncoder.encode("jsmith123"), "", null);
        String token = UUID.randomUUID ().toString ();
        PasswordReset passwordReset = new PasswordReset(1, user, token);
        user.setPasswordReset(passwordReset);
        String newPassword = "johnjohn123";
        passwordResetService.changePasswordAndDeleteToken (user, newPassword);
        Mockito.verify(this.userRepo, Mockito.times(1)).save(user);
        Mockito.verify(this.passwordResetRepo, Mockito.times(1)).deleteById (user.getPasswordReset ().getId ());
        assertTrue(passwordEncoder.matches(newPassword, user.getPassword()));
    }
}