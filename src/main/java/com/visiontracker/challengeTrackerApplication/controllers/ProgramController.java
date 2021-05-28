package com.visiontracker.challengeTrackerApplication.controllers;

import com.visiontracker.challengeTrackerApplication.models.datamodels.CreateProgramReq;
import com.visiontracker.challengeTrackerApplication.models.db.Program;
import com.visiontracker.challengeTrackerApplication.models.db.User;
import com.visiontracker.challengeTrackerApplication.repositories.ProgramRepository;
import com.visiontracker.challengeTrackerApplication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
    public ResponseEntity<Object> createProgram(@RequestBody CreateProgramReq createProgramReq)
    {
        if(createProgramReq == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Create Program Request");
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
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Program must be assigned to a program manager");
            }

            User programManager = userRepository.findUserById(createProgramReq.getUserId());
            createProgramReq.getProgram().setProgramManager(programManager);
            createProgramReq.getProgram().getProgramManager().getProgramsManaging().add(createProgramReq.getProgram());

            if (!createProgramReq.getUserIds().contains(programManager.getUserId()))
            {
                createProgramReq.getUserIds().add(programManager.getUserId());
            }

            for (Long u : createProgramReq.getUserIds())
            {
                User user = userRepository.findUserById(u);
                createProgramReq.getProgram().getUserList().add(user);
                user.getEnrolledPrograms().add(createProgramReq.getProgram());
            }

            Program newProgram = programRepository.save(createProgramReq.getProgram());

            return new ResponseEntity<>(newProgram.getProgramId(),HttpStatus.OK);
        }
        catch (DataIntegrityViolationException ex)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Duplicate Program Title");
        }
        catch (PersistenceException ex)
        {
            System.out.println("Persistence Exception");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Persistence Exception");
        }
        catch (ParseException ex)
        {
            System.out.println("Date parsed incorrectly");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Date parsed incorrectly");
        }
        catch(Exception ex)
        {
            System.out.println("Internal Server Error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @GetMapping(path = "/getEnrolledPrograms")
    public ResponseEntity<List<Program>> getEnrolledPrograms(@RequestParam(name = "userId") Long userId)
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

        return new ResponseEntity<>(programs, HttpStatus.OK);
    }

    @GetMapping(path = "getEnrolledPrograms/{programId}")
    public ResponseEntity<Object> retrieveProgram(@PathVariable(name = "programId") Long programId)
    {
        try
        {
            Program program = programRepository.findProgramById(programId);

            if (program == null)
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Program not found");
            }

            program.getUserList().clear();
            program.getMilestoneList().clear();
            program.getProgramManager().getEnrolledPrograms().clear();
            program.getProgramManager().getProgramsManaging().clear();
            program.getProgramManager().getMilestonesCreated().clear();
            program.getProgramManager().getMilestoneList().clear();

            return new ResponseEntity<>(program, HttpStatus.OK);
        }
        catch (Exception ex)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error");
        }
    }
}
