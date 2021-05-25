/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visiontracker.challengeTrackerApplication.controllers;

import com.visiontracker.challengeTrackerApplication.models.datamodels.LoginReq;
import com.visiontracker.challengeTrackerApplication.models.db.User;
import com.visiontracker.challengeTrackerApplication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UserUsernameExistException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
//import javax.ws.rs.*;
//import javax.ws.rs.core.*;
//import javax.ws.rs.core.Response.Status;
import javax.persistence.PersistenceException;
import javax.xml.ws.Response;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * REST Web Service
 *
 * @author sthit
 */
@Controller
@RequestMapping(path="/User")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping(path = "/register")
    public ResponseEntity<String> createUser(@RequestBody User newUser) {
        /*if (newUser == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new user request").build();
        }
        try {
            Long userEntityId = userEntitySessionBeanLocal.createNewUser(newUser);
            return Response.status(Response.Status.OK).entity(userEntityId).build();
        } catch (UserUsernameExistException | UnknownPersistenceException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (InputDataValidationException ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } */
        try
        {
            userRepository.save(newUser);

            return new ResponseEntity<String>(HttpStatus.ACCEPTED);
        }
        catch (PersistenceException ex)
        {
            if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
            }
        }

    }

    @PostMapping(path = "/login")
    public ResponseEntity<String> userLogin(LoginReq loginReq) throws InvalidLoginCredentialException {
        /*try {
            User userEntity = userEntitySessionBeanLocal.userLogin(loginReq.getUsername(), loginReq.getPassword());
            userEntity.getEnrolledPrograms().clear();
            userEntity.getMilestoneList().clear();
            userEntity.getMilestonesCreated().clear();
            userEntity.getProgramsManaging().clear();
            System.out.println("********** UserResource.userLogin(): User " + userEntity.getUsername() + " login remotely via web service");  
            
            return Response.status(Status.OK).entity(userEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }*/
        User user = userRepository.findUserByUsername(loginReq.getUsername());

        if (user == null)
        {
            return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
        }

        if (user.getPassword() != loginReq.getPassword())
        {
            return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<String>(HttpStatus.ACCEPTED);

    }

    @GetMapping(path = "/retrieveAllUsers")
    public ResponseEntity<String> retrieveAllUsers()
    {
        /*
        try
        {
            List<User> users = userEntitySessionBeanLocal.retrieveAllUsers();
            
            for(User user : users)
            {
                for (Program program : user.getEnrolledPrograms())
                {
                    program.getMilestoneList().clear();
                    program.getUserList().clear();
                }
                user.getEnrolledPrograms().clear();
                for (Program program : user.getProgramsManaging())
                {
                    program.getMilestoneList().clear();
                    program.getUserList().clear();
                }
                user.getProgramsManaging().clear();
                user.getMilestoneList().clear();
                user.getMilestonesCreated().clear();
            }
            
            GenericEntity<List<User>> genericEntity = new GenericEntity<List<User>>(users) {
            };
            
            return Response.status(Status.OK).entity(genericEntity).build();
        }    
        catch(Exception ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
        */
        try
        {
            Iterable<User> users = userRepository.findAll();

            return new ResponseEntity<String>(HttpStatus.ACCEPTED);
        }
        catch (Exception ex)
        {
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
