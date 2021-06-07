package com.visiontracker.challengeTrackerApplication.controllers;

import com.visiontracker.challengeTrackerApplication.models.datamodels.CreateMilestoneReq;
import com.visiontracker.challengeTrackerApplication.models.datamodels.UpdateMilestoneReq;
import com.visiontracker.challengeTrackerApplication.repositories.ProgramRepository;
import com.visiontracker.challengeTrackerApplication.services.MilestoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import util.exception.*;

import java.text.ParseException;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path="/Milestone")
public class MilestoneController {
    @Autowired
    private MilestoneService milestoneService;

    @PostMapping(path = "/createMilestone")
    public ResponseEntity<Object> createMilestone(@RequestBody CreateMilestoneReq createMilestoneReq)
    {
        try
        {
            return milestoneService.createMilestone(createMilestoneReq);
        }
        catch(CreateNewMilestoneException | MilestoneTitleExistException | ProgramNotFoundException | ParseException ex)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping(path = "/getProgramMilestones")
    public ResponseEntity<Object> retrieveProgramMilestones(@RequestParam(name = "programId") Long programId)
    {
        try
        {
            return milestoneService.retrieveProgramMilestones(programId);
        }
        catch (ProgramNotFoundException ex)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping(path = "/{milestoneId}")
    public ResponseEntity<Object> retrieveMilestone(@PathVariable(name = "milestoneId") Long milestoneId)
    {
        try
        {
            return milestoneService.retrieveMilestone(milestoneId);
        }
        catch (MilestoneNotFoundException ex)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PutMapping(path = "/editMilestone/{milestoneId}")
    public ResponseEntity<Object> editMilestone(@PathVariable(name = "milestoneId") Long milestoneId
                                                , @RequestBody UpdateMilestoneReq updateMilestoneReq)
    {
        try
        {
            return milestoneService.editMilestone(milestoneId, updateMilestoneReq);
        }
        catch(MilestoneTitleExistException | UpdateMilestoneException | ParseException ex)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @DeleteMapping("/deleteMilestone/{milestoneId}")
    public ResponseEntity<Object> deleteMilestone(@PathVariable(name = "milestoneId") Long milestoneId)
    {
        try
        {
            return milestoneService.deleteMilestone(milestoneId);
        }
        catch (MilestoneNotFoundException ex)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        catch (Exception ex)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
}
