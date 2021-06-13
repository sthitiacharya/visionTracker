package com.visiontracker.challengeTrackerApplication.controllers;

import com.visiontracker.challengeTrackerApplication.models.datamodels.CreateMilestoneReq;
import com.visiontracker.challengeTrackerApplication.models.datamodels.UpdateMilestoneReq;
import com.visiontracker.challengeTrackerApplication.services.MilestoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Object> createMilestone(@RequestBody CreateMilestoneReq createMilestoneReq) throws CreateNewMilestoneException, ParseException, ProgramNotFoundException, MilestoneTitleExistException {
            return milestoneService.createMilestone(createMilestoneReq);
    }

    @GetMapping(path = "/getProgramMilestones")
    public ResponseEntity<Object> retrieveProgramMilestones(@RequestParam(name = "programId") Long programId)
    {
            return milestoneService.retrieveProgramMilestones(programId);
    }

    @GetMapping(path = "/{milestoneId}")
    public ResponseEntity<Object> retrieveMilestone(@PathVariable(name = "milestoneId") Long milestoneId) throws MilestoneNotFoundException {
            return milestoneService.retrieveMilestone(milestoneId);
    }

    @PutMapping(path = "/editMilestone/{milestoneId}")
    public ResponseEntity<Object> editMilestone(@PathVariable(name = "milestoneId") Long milestoneId
                                                , @RequestBody UpdateMilestoneReq updateMilestoneReq) throws ParseException, UpdateMilestoneException {
            return milestoneService.editMilestone(milestoneId, updateMilestoneReq);
    }

    @DeleteMapping("/deleteMilestone/{milestoneId}")
    public ResponseEntity<Object> deleteMilestone(@PathVariable(name = "milestoneId") Long milestoneId) throws MilestoneNotFoundException {
            return milestoneService.deleteMilestone(milestoneId);
    }

    @GetMapping("/getReminders")
    public ResponseEntity<Object> getReminders(@RequestParam(name = "userId") Long userId)
    {
            return milestoneService.getReminders(userId);
    }
}
