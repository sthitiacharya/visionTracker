package com.visiontracker.challengeTrackerApplication.controllers;

import com.visiontracker.challengeTrackerApplication.models.db.ProgressHistory;
import com.visiontracker.challengeTrackerApplication.services.ProgressHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Object> createProgressHistoryRecord(@RequestBody ProgressHistory progressHistory) {
        try
        {
            return progressHistoryService.createProgressHistoryRecord(progressHistory);
        }
        catch (MilestoneNotFoundException | ProgramNotFoundException ex)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        catch (Exception ex)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/progressHistories/{milestoneId}")
    public ResponseEntity<Object> getProgressHistoriesByMilestone(@PathVariable(name = "milestoneId") Long milestoneId)
    {
        try
        {
            return progressHistoryService.retrieveProgressHistoriesByMilestone(milestoneId);
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

    @PutMapping("/editProgressHistory")
    public ResponseEntity<Object> editProgressHistory(@RequestBody ProgressHistory progressHistory)
    {
        try
        {
            return progressHistoryService.editProgressHistoryRecord(progressHistory);
        }
        catch (ProgramNotFoundException | ProgressHistoryNotFoundException | MilestoneNotFoundException ex)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        catch (Exception ex)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @DeleteMapping("/deleteProgressHistory")
    public ResponseEntity<Object> deleteProgressHistory(@RequestBody Long progressHistoryId)
    {
        try
        {
            return progressHistoryService.deleteProgressHistoryRecord(progressHistoryId);
        }
        catch (ProgressHistoryNotFoundException ex)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        catch (Exception ex)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
}
