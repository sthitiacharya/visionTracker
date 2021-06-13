package com.visiontracker.challengeTrackerApplication.controllers;

import com.visiontracker.challengeTrackerApplication.models.datamodels.CreateProgHistoryReq;
import com.visiontracker.challengeTrackerApplication.models.db.ProgressHistory;
import com.visiontracker.challengeTrackerApplication.services.ProgressHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import util.exception.MilestoneNotFoundException;
import util.exception.ProgramNotFoundException;
import util.exception.ProgressHistoryNotFoundException;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path="/ProgressHistory")
public class ProgressHistoryController {
    @Autowired
    private ProgressHistoryService progressHistoryService;

    @PostMapping(path = "/logProgress")
    public ResponseEntity<Object> createProgressHistoryRecord(@RequestBody CreateProgHistoryReq progressHistory) throws MilestoneNotFoundException, ProgramNotFoundException {
        return progressHistoryService.createProgressHistoryRecord(progressHistory);
    }

    @GetMapping("/progressHistories/{milestoneId}")
    public ResponseEntity<Object> getProgressHistoriesByMilestone(@PathVariable(name = "milestoneId") Long milestoneId) throws MilestoneNotFoundException {
        return progressHistoryService.retrieveProgressHistoriesByMilestone(milestoneId);
    }

    @GetMapping("/progressHistory/{progressHistoryId}")
    public ResponseEntity<Object> retrieveProgressHistory(@PathVariable(name = "progressHistoryId") Long progressHistoryId) throws ProgressHistoryNotFoundException {
        return progressHistoryService.retrieveProgressHistory(progressHistoryId);
    }

    @PutMapping("/editProgressHistory")
    public ResponseEntity<Object> editProgressHistory(@RequestBody ProgressHistory progressHistory) throws MilestoneNotFoundException, ProgramNotFoundException, ProgressHistoryNotFoundException {
        return progressHistoryService.editProgressHistoryRecord(progressHistory);
    }

    @DeleteMapping("/deleteProgressHistory/{progressHistoryId}")
    public ResponseEntity<Object> deleteProgressHistory(@PathVariable(name = "progressHistoryId") Long progressHistoryId) throws ProgressHistoryNotFoundException {
        return progressHistoryService.deleteProgressHistoryRecord(progressHistoryId);
    }
}
