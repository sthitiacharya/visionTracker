/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visiontracker.challengeTrackerApplication.controllers;

import com.visiontracker.challengeTrackerApplication.models.datamodels.LoginReq;
import com.visiontracker.challengeTrackerApplication.models.db.Program;
import com.visiontracker.challengeTrackerApplication.models.db.User;
import com.visiontracker.challengeTrackerApplication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import util.exception.InvalidLoginCredentialException;

import javax.persistence.PersistenceException;
import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path="/User")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    private String username;
    private String password;
    private List<Program> enrolledPrograms;

    @PostMapping(path = "/register")
    public ResponseEntity<Integer> createUser(@RequestBody User newUser) {
        try
        {
            userRepository.save(newUser);

            return new ResponseEntity<>(newUser.getUserId(), HttpStatus.ACCEPTED);
        }
        catch (PersistenceException ex)
        {
            if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

    }

    @PostMapping(path = "/login")
    public ResponseEntity<User> userLogin(@RequestBody LoginReq loginReq) throws InvalidLoginCredentialException {
        System.out.println("Login Req username: " + loginReq.getUsername());
        User user = userRepository.findUserByUsername(loginReq.getUsername());

        if (user == null)
        {
            System.out.println("Username not found");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        System.out.println("User Password: " + user.getPassword());
        System.out.println("LoginReq Password: " + loginReq.getPassword());

        if (!user.getPassword().equals(loginReq.getPassword()))
        {
            System.out.println("Invalid Password");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        setEnrolledPrograms(user.getEnrolledPrograms());

        user.getMilestoneList().clear();
        user.getProgramsManaging().clear();
        user.getEnrolledPrograms().clear();
        user.getMilestonesCreated().clear();

        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
    }

    @GetMapping(path = "/retrieveAllUsers")
    public ResponseEntity<List<User>> retrieveAllUsers()
    {
        try
        {
            List<User> users = userRepository.findAll();

            for (User u : users)
            {
                u.getProgramsManaging().clear();
                u.getEnrolledPrograms().clear();
                u.getMilestoneList().clear();
                u.getMilestonesCreated().clear();
            }

            return new ResponseEntity<>(users, HttpStatus.ACCEPTED);
        }
        catch (Exception ex)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Program> getEnrolledPrograms() {
        return enrolledPrograms;
    }

    public void setEnrolledPrograms(List<Program> enrolledPrograms) {
        this.enrolledPrograms = enrolledPrograms;
    }
}
