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
    public ResponseEntity<Object> createProgram(@RequestBody CreateProgramReq createProgramReq) throws ProgramTitleExistException, CreateNewProgramException {
        return programService.createProgram(createProgramReq);
    }

    @GetMapping(path = "/getEnrolledPrograms")
    public ResponseEntity<Object> getEnrolledPrograms(@RequestParam(name = "userId") Long userId) throws UserNotFoundException {
        return programService.getEnrolledPrograms(userId);
    }

    @GetMapping(path = "getEnrolledPrograms/{programId}")
    public ResponseEntity<Object> retrieveProgram(@PathVariable(name = "programId") Long programId) throws ProgramNotFoundException {
        return programService.retrieveProgram(programId);
    }

    @PutMapping("/editProgram")
    public ResponseEntity<Object> editProgram(@RequestBody UpdateProgramReq updateProgramReq) throws UserNotFoundException, ProgramTitleExistException, UpdateProgramException {
            return programService.editProgram(updateProgramReq);
    }

    @DeleteMapping("/deleteProgram/{programId}")
    public ResponseEntity<Object> deleteProgram(@PathVariable(name = "programId") Long programId) throws ProgramNotFoundException {
            return programService.deleteProgram(programId);
    }
}
