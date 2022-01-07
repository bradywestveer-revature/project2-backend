package com.revature.project2backend.services;

import com.revature.project2backend.exceptions.NotFoundException;
import com.revature.project2backend.models.User;
import com.revature.project2backend.repositories.UserRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    UserService userService;
    UserRepo userRepo = Mockito.mock(UserRepo.class);

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

    @Test //todo make encryption test
    void createUserPasswordEncryptTest(){

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
        System.out.println(expectedResult.get(0));
        System.out.println(actualResult);

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
    void getUserByPasswordResetId() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void loginUser() {
    }

    @Test
    void getUserByPasswordResetToken() {
    }

    @Test //todo get confirmation on method stability
    void updateUserAlwaysEncrypt() {
    }
}