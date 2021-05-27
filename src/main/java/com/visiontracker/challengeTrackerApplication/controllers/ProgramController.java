package com.visiontracker.challengeTrackerApplication.controllers;

import com.visiontracker.challengeTrackerApplication.models.datamodels.CreateProgramReq;
import com.visiontracker.challengeTrackerApplication.models.db.Program;
import com.visiontracker.challengeTrackerApplication.models.db.User;
import com.visiontracker.challengeTrackerApplication.repositories.ProgramRepository;
import com.visiontracker.challengeTrackerApplication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PersistenceException;
import javax.websocket.server.PathParam;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path="/Program")
public class ProgramController {
    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private UserRepository userRepository;

    private UserController userController;

    @PostMapping("/createProgram")
    public ResponseEntity<Integer> createProgram(@RequestBody CreateProgramReq createProgramReq)
    {
        if(createProgramReq == null) {
            return new ResponseEntity<> (HttpStatus.BAD_REQUEST);
            //"Invalid Create Program Request"
        }
        try
        {
            System.out.println("In createProgram RESTful Web Service");

            Date date = new SimpleDateFormat("dd-MM-yyyy").parse(createProgramReq.getStartDate());
            createProgramReq.getProgram().setStartDate(date);

            Date date2 = new SimpleDateFormat("dd-MM-yyyy").parse(createProgramReq.getTargetCompletionDate());
            createProgramReq.getProgram().setTargetCompletionDate(date2);

            if (createProgramReq.getUserId() == null)
            {
                System.out.println("Program must be assigned to a program manager");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            User programManager = userRepository.findUserById(createProgramReq.getUserId());
            createProgramReq.getProgram().setProgramManager(programManager);
            createProgramReq.getProgram().getProgramManager().getProgramsManaging().add(createProgramReq.getProgram());

            if (!createProgramReq.getUserIds().contains(programManager.getUserId()))
            {
                createProgramReq.getUserIds().add(programManager.getUserId());
            }

            for (Integer u : createProgramReq.getUserIds())
            {
                User user = userRepository.findUserById(u);
                createProgramReq.getProgram().getUserList().add(user);
                user.getEnrolledPrograms().add(createProgramReq.getProgram());
            }

            Program newProgram = programRepository.save(createProgramReq.getProgram());

            return new ResponseEntity<Integer>(newProgram.getProgramId(),HttpStatus.ACCEPTED);
        }
        catch (PersistenceException ex)
        {
            System.out.println("Persistence Exception");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        catch (ParseException ex)
        {
            System.out.println("Date parsed incorrectly");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        catch(Exception ex)
        {
            System.out.println("Internal Server Error");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/getEnrolledPrograms")
    public ResponseEntity<List<Program>> getEnrolledPrograms(@RequestParam(name = "userId") Integer userId)
    {
        User user = userRepository.findUserById(userId);
        if (user == null)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<Program> programs = programRepository.findAll();
        List<Program> enrolledPrograms = new ArrayList<>();

        for (Program p : programs)
        {
            if (p.getUserList().contains(user))
            {
                enrolledPrograms.add(p);
            }
            p.getUserList().clear();
            p.getMilestoneList().clear();
        }
        user.getMilestonesCreated().clear();
        user.getEnrolledPrograms().clear();
        user.getProgramsManaging().clear();
        user.getMilestoneList().clear();

        return new ResponseEntity<>(programs, HttpStatus.ACCEPTED);
    }

    @GetMapping(path = "getEnrolledPrograms/{programId}")
    public ResponseEntity<Program> retrieveProgram(@PathVariable(value = "programId") Integer programId)
    {
        Program program = programRepository.findProgramById(programId);

        program.getUserList().clear();
        program.getMilestoneList().clear();

        return new ResponseEntity<>(program, HttpStatus.ACCEPTED);
    }
}
