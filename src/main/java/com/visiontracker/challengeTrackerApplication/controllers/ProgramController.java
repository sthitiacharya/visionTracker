package com.visiontracker.challengeTrackerApplication.controllers;

import com.visiontracker.challengeTrackerApplication.models.datamodels.CreateProgramReq;
import com.visiontracker.challengeTrackerApplication.models.datamodels.UpdateProgramReq;
import com.visiontracker.challengeTrackerApplication.repositories.UserRepository;
import com.visiontracker.challengeTrackerApplication.services.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import util.exception.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path="/Program")
public class ProgramController {
    @Autowired
    private ProgramService programService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/createProgram")
    public ResponseEntity<Object> createProgram(@RequestBody CreateProgramReq createProgramReq)
    {
        try
        {
            return programService.createProgram(createProgramReq);
        }
        catch (CreateNewProgramException | ProgramTitleExistException ex)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getClass());
        }
    }

    @GetMapping(path = "/getEnrolledPrograms")
    public ResponseEntity<Object> getEnrolledPrograms(@RequestParam(name = "userId") Long userId)
    {
        try
        {
            return programService.getEnrolledPrograms(userId);
        }
        catch (UserNotFoundException ex)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping(path = "getEnrolledPrograms/{programId}")
    public ResponseEntity<Object> retrieveProgram(@PathVariable(name = "programId") Long programId)
    {
        try
        {
            return programService.retrieveProgram(programId);
        }
        catch (ProgramNotFoundException ex)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PutMapping("/editProgram")
    public ResponseEntity<Object> editProgram(@RequestBody UpdateProgramReq updateProgramReq)
    {
        try
        {
            return programService.editProgram(updateProgramReq);
        }
        catch (UpdateProgramException | ProgramTitleExistException | UserNotFoundException ex)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        catch(Exception ex)
        {
            System.out.println("Internal Server Error");
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getClass());
        }
    }
}
