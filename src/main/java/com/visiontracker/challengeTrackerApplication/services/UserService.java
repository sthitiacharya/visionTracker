package com.visiontracker.challengeTrackerApplication.services;

import com.visiontracker.challengeTrackerApplication.helper.UtilClass;
import com.visiontracker.challengeTrackerApplication.models.datamodels.LoginReq;
import com.visiontracker.challengeTrackerApplication.models.db.Program;
import com.visiontracker.challengeTrackerApplication.models.db.User;
import com.visiontracker.challengeTrackerApplication.repositories.ProgramRepository;
import com.visiontracker.challengeTrackerApplication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import util.exception.InvalidLoginCredentialException;
import util.exception.ProgramNotFoundException;
import util.exception.UserUsernameExistException;

import javax.persistence.PersistenceException;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProgramRepository programRepository;

    private UtilClass util = new UtilClass();

    public ResponseEntity<Object> createUser(User newUser) throws UserUsernameExistException {
        try
        {
            if (userRepository.findUserByUsername(newUser.getUsername()) != null)
            {
                throw new UserUsernameExistException("Duplicate username");
            }
            newUser.setRewardPoints(0);
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

        util.clearUserLists(user);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    public ResponseEntity<List<User>> retrieveAllUsers()
    {
        try
        {
            List<User> users = userRepository.findAll();

            for (User u : users)
            {
                util.clearUserLists(u);
            }

            return new ResponseEntity<>(users, HttpStatus.OK);
        }
        catch (Exception ex)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> retrieveUsersByProgramId(Long programId) throws ProgramNotFoundException {
        Program program = programRepository.findProgramByProgramId(programId);

        if (program == null)
        {
            throw new ProgramNotFoundException("Program not found");
        }

        List<User> users = userRepository.findUsersByProgramId(program);
        util.clearProgramLists(program);
        for (User u : users) { util.clearUserLists(u); }

        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
