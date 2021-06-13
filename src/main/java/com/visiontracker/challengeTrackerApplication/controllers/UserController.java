
package com.visiontracker.challengeTrackerApplication.controllers;

import com.visiontracker.challengeTrackerApplication.models.datamodels.LoginReq;
import com.visiontracker.challengeTrackerApplication.models.db.User;
import com.visiontracker.challengeTrackerApplication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import util.exception.InvalidLoginCredentialException;
import util.exception.UserUsernameExistException;

import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path="/User")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(path = "/register")
    public ResponseEntity<Object> createUser(@RequestBody User newUser) throws UserUsernameExistException {
        return userService.createUser(newUser);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<Object> userLogin(@RequestBody LoginReq loginReq) throws InvalidLoginCredentialException {
        return userService.userLogin(loginReq);
    }

    @GetMapping(path = "/retrieveAllUsers")
    public ResponseEntity<List<User>> retrieveAllUsers()
    {
        return userService.retrieveAllUsers();
    }
}
