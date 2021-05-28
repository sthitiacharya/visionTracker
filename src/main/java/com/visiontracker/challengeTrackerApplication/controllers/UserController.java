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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import util.exception.InvalidLoginCredentialException;

import javax.persistence.PersistenceException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
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
    public ResponseEntity<Object> createUser(@RequestBody User newUser) {
        try
        {
            userRepository.save(newUser);

            return new ResponseEntity<>(newUser.getUserId(), HttpStatus.OK);
        }
        catch (DataIntegrityViolationException ex)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Duplicate username or email");
        }
        catch (PersistenceException ex)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PostMapping(path = "/login")
    public ResponseEntity<Object> userLogin(@RequestBody LoginReq loginReq) throws InvalidLoginCredentialException {
        System.out.println("Login Req username: " + loginReq.getUsername());
        User user = userRepository.findUserByUsername(loginReq.getUsername());

        if (user == null)
        {
            System.out.println("Username not found");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username not found");
        }
        System.out.println("User Password: " + user.getPassword());
        System.out.println("LoginReq Password: " + loginReq.getPassword());

        if (!user.getPassword().equals(loginReq.getPassword()))
        {
            System.out.println("Invalid Password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Password");
        }

        setEnrolledPrograms(user.getEnrolledPrograms());

        user.getMilestoneList().clear();
        user.getProgramsManaging().clear();
        user.getEnrolledPrograms().clear();
        user.getMilestonesCreated().clear();

        return new ResponseEntity<>(user, HttpStatus.OK);
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

            return new ResponseEntity<>(users, HttpStatus.OK);
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
