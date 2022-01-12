package com.revature.project2backend.services;

import com.revature.project2backend.exceptions.InvalidCredentialsException;
import com.revature.project2backend.exceptions.NotFoundException;
import com.revature.project2backend.models.User;
import com.revature.project2backend.repositories.UserRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    UserService userService;
    UserRepo userRepo = Mockito.mock(UserRepo.class);
    private final BCryptPasswordEncoder passwordEncoder= new BCryptPasswordEncoder();

    public UserServiceTest(){
        this.userService = new UserService(userRepo);
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createUser() {

        User mockUser = new User("David", "Helfer", "yes@gmail.com", "username", "password");

        userService.createUser(mockUser);

        Mockito.verify(userRepo).save(mockUser);
    }

    @Test
    void createUserPasswordEncryptTest(){
        /**
        * Test the password encryption
        * */
        String password = "jsmith1235678";
        User user = new User(1, "John", "Smith", "johnsmith@javadev.com", "jsmith", password, "", null);
        userService.createUser(user);
        assertTrue(passwordEncoder.matches(password, user.getPassword()));
        Mockito.verify(userRepo).save(user);
    }

    @Test
    void getUsers() {

        List<User> expectedResult = new ArrayList<>();

        expectedResult.add(new User("David", "Helfer", "yes@gmail.com", "username", "password"));
        expectedResult.add(new User("David", "Helfer", "yes@gmail.com", "username", "password"));
        expectedResult.add(new User("David", "Helfer", "yes@gmail.com", "username", "password"));
        expectedResult.add(new User("David", "Helfer", "yes@gmail.com", "username", "password"));
        Mockito.when(userRepo.findAll()).thenReturn(expectedResult);

        List<User> actualResult = userService.getUsers();

        assertEquals(expectedResult, actualResult);



    }

    @Test
    void getUser() throws NotFoundException {
        List<User> expectedResult = new ArrayList<>();

        expectedResult.add(new User(1, "David", "Helfer", "yes@gmail.com", "username", "password"));
        expectedResult.add(new User(2, "a", "Helfer", "yes@gmail.com", "username", "password"));
        expectedResult.add(new User(3, "b", "Helfer", "yes@gmail.com", "username", "password"));
        expectedResult.add(new User(4, "c", "Helfer", "yes@gmail.com", "username", "password"));
        Mockito.when(userRepo.findById(1)).thenReturn(Optional.ofNullable(expectedResult.get(0)));

        User actualResult = userService.getUser(1);

        assertEquals(expectedResult.get(0), actualResult);
    }

    @Test
    void getUserByUsername() {
        List<User> expectedResult = new ArrayList<>();

        expectedResult.add(new User("David", "Helfer", "yes@gmail.com", "username", "password"));
        expectedResult.add(new User("a", "Helfer", "yes@gmail.com", "d", "password"));
        expectedResult.add(new User("b", "Helfer", "yes@gmail.com", "e", "password"));
        expectedResult.add(new User("c", "Helfer", "yes@gmail.com", "f", "password"));
        Mockito.when(userRepo.findByUsername("username")).thenReturn(expectedResult.get(0));

        User actualResult = userService.getUserByUsername("username");

        assertEquals(expectedResult.get(0), actualResult);
    }

    @Test
    void getUserByEmail() {
        List<User> expectedResult = new ArrayList<>();

        expectedResult.add(new User("David", "Helfer", "yes@gmail.com", "username", "password"));
        expectedResult.add(new User("a", "Helfer", "g", "d", "password"));
        expectedResult.add(new User("b", "Helfer", "h", "e", "password"));
        expectedResult.add(new User("c", "Helfer", "i", "f", "password"));
        Mockito.when(userRepo.findByEmail("yes@gmail.com")).thenReturn(expectedResult.get(0));

        User actualResult = userService.getUserByEmail("yes@gmail.com");

        assertEquals(expectedResult.get(0), actualResult);
    }

    @Test
    void updateUser_PasswordChanged() throws NotFoundException{
        /*
         * Test password was changed case
        * */
        String oldPassword = passwordEncoder.encode("jsmith123");
        User userBeforeChange = new User(1, "John", "Smith", "johnsmith@javadev.com", "jsmith", oldPassword, "", null);
        Integer id = 1;
        Mockito.when(this.userRepo.findById (id)).thenReturn (java.util.Optional.of(userBeforeChange));
        /*
         * When the user is changing their profile which includes password or email, etc. updating feature the initial password will come across as UN-encrypted
         * A non-changed password should not result in the encryption of the already-encrypted password
         * */
        String newPassword = "jsmith321";
        User userAfterPasswordChange = new User(1, "John", "Smith", "johnsmith@javadev.com", "jsmith", newPassword, "", null);
        userService.updateUser(userAfterPasswordChange);
        assertTrue(passwordEncoder.matches(newPassword, userAfterPasswordChange.getPassword()));
        Mockito.verify(this.userRepo, Mockito.times(1)).save(userAfterPasswordChange);
    }


    @Test
    void updateUser_PasswordNOTChanged() throws NotFoundException{
        /*
         * Test password was NOT changed case
         * */
        String oldPassword = passwordEncoder.encode("jsmith123");
        User userBeforeChange = new User(1, "John", "Smith", "johnsmith@javadev.com", "jsmith", oldPassword, "", null);
        Integer id = 1;
        Mockito.when(this.userRepo.findById (id)).thenReturn (java.util.Optional.of(userBeforeChange));
        /*
         * When the user is changing their profile which includes password or email, etc. and did not touch the password field, then the user.password will come across
         * as what is from the database or ENCRYPTED, unlike in the case where they touched the password field and changed it on their UI which will come across
         * UN-encrypted.  This tests the no-change, didn't touch field case.
         * */
        String newPassword = oldPassword;
        User userAfterPasswordChange = new User(1, "John", "Smith", "johnsmith@javadev.com", "jsmith", newPassword, "", null);
        userService.updateUser(userAfterPasswordChange);
        assertTrue(passwordEncoder.matches("jsmith123", userAfterPasswordChange.getPassword()));
        Mockito.verify(this.userRepo, Mockito.times(1)).save(userAfterPasswordChange);
    }

    @Test
    void loginUser_ValidUsernameAndPassword() throws InvalidCredentialsException {
        String username ="jsmith";
        String password = "password123";
        String encryptedPassword = passwordEncoder.encode(password);
        User expectedResult = new User(1, "John", "Smith", "johnsmith@javadev.com", username, encryptedPassword, "", null);
        Mockito.when(this.userRepo.findByUsername (username)).thenReturn(expectedResult);
        User actualResult = userService.loginUser(username, password);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void loginUser_InvalidUsername() {
        String username ="jsmith";
        String email= "johnsmith@javadev.com";
        String password = "password123";
        String encryptedPassword = passwordEncoder.encode(password);
        User expectedResult = new User(1, "John", "Smith", email, username, encryptedPassword, "", null);

        Mockito.when(this.userRepo.findByUsername (username)).thenReturn(expectedResult);
        Mockito.when(this.userRepo.findByEmail (email)).thenReturn(expectedResult);
        /**
         * username is not valid
         * */
        assertThrows (InvalidCredentialsException.class, () -> {
            userService.loginUser("j", password);
        });
    }

    @Test
    void loginUser_ValidUsernameInvalidPassword() {
        String username ="jsmith";
        String email= "johnsmith@javadev.com";
        String password = "password123";
        String encryptedPassword = passwordEncoder.encode(password);
        User expectedResult = new User(1, "John", "Smith", email, username, encryptedPassword, "", null);

        Mockito.when(this.userRepo.findByUsername (username)).thenReturn(expectedResult);
        Mockito.when(this.userRepo.findByEmail (email)).thenReturn(expectedResult);
        /**
         * username is valid, but password is not
         * */
        assertThrows (InvalidCredentialsException.class, () -> {
            userService.loginUser(username, "password12");
        });
    }

    @Test
    void loginUser_EmailAndPasswordValid() throws InvalidCredentialsException {
        String username ="jsmith";
        String email= "johnsmith@javadev.com";
        String password = "password123";
        String encryptedPassword = passwordEncoder.encode(password);
        User expectedResult = new User(1, "John", "Smith", email, username, encryptedPassword, "", null);

        Mockito.when(this.userRepo.findByUsername (username)).thenReturn(expectedResult);
        Mockito.when(this.userRepo.findByEmail (email)).thenReturn(expectedResult);
        /**
         * They logged in with email and password, and both fields are valid
         * */
        User actualResult = userService.loginUser(email, password);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void loginUser_EmailInvalid() {
        String username ="jsmith";
        String email= "johnsmith@javadev.com";
        String password = "password123";
        String encryptedPassword = passwordEncoder.encode(password);
        User expectedResult = new User(1, "John", "Smith", email, username, encryptedPassword, "", null);

        Mockito.when(this.userRepo.findByUsername (username)).thenReturn(expectedResult);
        Mockito.when(this.userRepo.findByEmail (email)).thenReturn(expectedResult);
        /**
         * They logged in with wrong email
         * */
        assertThrows (InvalidCredentialsException.class, () -> {
                    userService.loginUser("johnsmith@javada.com", password);
        });
    }

    @Test
    void loginUser_EmailValidPasswordInvalid() {
        String username ="jsmith";
        String email= "johnsmith@javadev.com";
        String password = "password123";
        String encryptedPassword = passwordEncoder.encode(password);
        User expectedResult = new User(1, "John", "Smith", email, username, encryptedPassword, "", null);

        Mockito.when(this.userRepo.findByUsername (username)).thenReturn(expectedResult);
        Mockito.when(this.userRepo.findByEmail (email)).thenReturn(expectedResult);
        /**
         * They logged in with right email but wrong password
         * */
        assertThrows (InvalidCredentialsException.class, () -> {
            userService.loginUser(email, "password12");
        });
    }

    @Test
    void getUserByPasswordResetToken_UserFound() {
        /**
         * See PasswordResetService::getUserByPasswordResetToken for tests of validation of the token parameter passed in this method in UserService does not do
         * the validations
         */
        String token = UUID.randomUUID ().toString ();
        String encryptedPassword = passwordEncoder.encode("johnjohn123");
        User expectedResult = new User(1, "John", "Smith", "johnsmith@javadev.com", "jsmith", encryptedPassword, "", null);
        Mockito.when(userRepo.findByPasswordResetToken(token)).thenReturn(expectedResult);
        User actualResult = userService.getUserByPasswordResetToken(token);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void getUserByPasswordResetToken_UserNOTFound() {
        /**
         * See PasswordResetService::getUserByPasswordResetToken for tests of validation of the token parameter passed in this method in UserService does not do
         * the validations
         */
        String token = UUID.randomUUID ().toString ();
        Mockito.when(userRepo.findByPasswordResetToken(token)).thenReturn(null);
        User actualResult = userService.getUserByPasswordResetToken(token);
        assertNull(actualResult);
    }

    @Test //todo get confirmation on method stability
    void updateUserAlwaysEncrypt() throws NotFoundException{
        /**
         * This method will always expect a UN-encrypted password so no need to check if password was or was not changed
         * in the UI, it will always be the un-encrypted password from the change-password page on front-end.
         * */
        String oldPassword = passwordEncoder.encode("jsmith123");
        User userBeforeChange = new User(1, "John", "Smith", "johnsmith@javadev.com", "jsmith", oldPassword, "", null);
        Integer id = 1;
        //Mockito.when(this.userRepo.findById (id)).thenReturn (java.util.Optional.of(userBeforeChange));
        String newPassword = "jsmith321";
        User userAfterPasswordChange = new User(1, "John", "Smith", "johnsmith@javadev.com", "jsmith", newPassword, "", null);
        userService.updateUserAlwaysEncrypt(userAfterPasswordChange, newPassword);
        assertTrue(passwordEncoder.matches(newPassword, userAfterPasswordChange.getPassword()));
        Mockito.verify(this.userRepo, Mockito.times(1)).save(userAfterPasswordChange);
    }
}