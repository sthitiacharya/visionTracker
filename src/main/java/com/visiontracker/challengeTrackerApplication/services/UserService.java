package com.visiontracker.challengeTrackerApplication.services;

import com.visiontracker.challengeTrackerApplication.models.datamodels.LoginReq;
import com.visiontracker.challengeTrackerApplication.models.db.Program;
import com.visiontracker.challengeTrackerApplication.models.db.User;
import com.visiontracker.challengeTrackerApplication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import util.exception.InvalidLoginCredentialException;
import util.exception.UserUsernameExistException;

import javax.persistence.PersistenceException;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<Object> createUser(User newUser) throws UserUsernameExistException {
        try
        {
            if (userRepository.findUserByUsername(newUser.getUsername()) != null)
            {
                throw new UserUsernameExistException("Duplicate username");
            }

            userRepository.save(newUser);

            return new ResponseEntity<>(newUser.getUserId(), HttpStatus.OK);
        }
        catch (PersistenceException ex)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    public ResponseEntity<Object> userLogin(LoginReq loginReq) throws InvalidLoginCredentialException {
        System.out.println("Login Req username: " + loginReq.getUsername());
        User user = userRepository.findUserByUsername(loginReq.getUsername());

        if (user == null)
        {
            throw new InvalidLoginCredentialException("Username not found");
        }

        if (!user.getPassword().equals(loginReq.getPassword()))
        {
            throw new InvalidLoginCredentialException("Invalid Password");
        }

        user.getMilestoneList().clear();
        user.getProgramsManaging().clear();
        user.getEnrolledPrograms().clear();
        user.getMilestonesCreated().clear();

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

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
}
