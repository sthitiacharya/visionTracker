package com.visiontracker.challengeTrackerApplication.controllers;

import com.visiontracker.challengeTrackerApplication.models.db.User;
import com.visiontracker.challengeTrackerApplication.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;


public class UserControllerTest {
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserController userController;

    @Test
    public void testController01()
    {
        User newUser = new User("email@email.com", "newUser", "password", "111 Address Avenue Singapore 123456");
        ResponseEntity<String> response = userController.createUser(newUser);
        //verify()
    }
}
