package com.visiontracker.challengeTrackerApplication.services;

import com.visiontracker.challengeTrackerApplication.models.db.ProgressHistory;
import com.visiontracker.challengeTrackerApplication.repositories.MilestoneRepository;
import com.visiontracker.challengeTrackerApplication.repositories.ProgramRepository;
import com.visiontracker.challengeTrackerApplication.repositories.ProgressHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import util.exception.MilestoneNotFoundException;
import util.exception.ProgramNotFoundException;
import util.exception.ProgressHistoryNotFoundException;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class ProgressHistoryService {
    @Autowired
    private ProgressHistoryRepository progressHistoryRepository;

    @Autowired
    private MilestoneRepository milestoneRepository;

    @Autowired
    private ProgramRepository programRepository;

    public ResponseEntity<Object> createProgressHistoryRecord(ProgressHistory progressHistory) throws ProgramNotFoundException, MilestoneNotFoundException {
        if (progressHistory.getProgramId() == null)
        {
            throw new ProgramNotFoundException("Program not found");
        }
        progressHistory.getProgramId().getUserList().clear();
        progressHistory.getProgramId().getMilestoneList().clear();

        if (progressHistory.getMilestoneId() == null)
        {
            throw new MilestoneNotFoundException("Milestone not found");
        }
        progressHistory.setDateOfRecord(new Date());
        ProgressHistory newProgLog = progressHistoryRepository.save(progressHistory);
        Double milestoneProgressRate = (progressHistory.getValue().divide(progressHistory.getMilestoneId().getTargetValue())).doubleValue();
        int numMilestones = milestoneRepository.findMilestonesByProgramId(progressHistory.getProgramId()).size();
        Double programProgressRate = newProgLog.getProgramId().getCurrentProgressRate() + (milestoneProgressRate/numMilestones);
        newProgLog.getProgramId().setCurrentProgressRate(programProgressRate);
        programRepository.save(newProgLog.getProgramId());
        return new ResponseEntity<>(progressHistory.getProgressHistoryId(), HttpStatus.OK);
    }

    public ResponseEntity<Object> retrieveProgressHistoriesByMilestone(Long milestoneId) throws MilestoneNotFoundException {
        if (milestoneId == null)
        {
            throw new MilestoneNotFoundException("Invalid milestone ID");
        }
        List<ProgressHistory> progressHistories = progressHistoryRepository.findProgressHistoriesByMilestoneId(milestoneId);
        for (ProgressHistory p : progressHistories)
        {
            p.getProgramId().getMilestoneList().clear();
            p.getProgramId().getUserList().clear();
        }
        return new ResponseEntity<>(progressHistories, HttpStatus.OK);
    }

    public ResponseEntity<Object> editProgressHistoryRecord(ProgressHistory progressHistory) throws ProgramNotFoundException, MilestoneNotFoundException, ProgressHistoryNotFoundException {
        ProgressHistory progressHistoryToUpdate = progressHistoryRepository.findProgressHistoryByProgressHistoryId(progressHistory.getProgressHistoryId());
        if (progressHistoryToUpdate == null)
        {
            throw new ProgressHistoryNotFoundException("Progress History not found");
        }

        if (progressHistory.getProgramId() == null)
        {
            throw new ProgramNotFoundException("Program not found");
        }
        progressHistoryToUpdate.setProgramId(progressHistory.getProgramId());
        progressHistory.getProgramId().getUserList().clear();
        progressHistory.getProgramId().getMilestoneList().clear();

        if (progressHistory.getMilestoneId() == null)
        {
            throw new MilestoneNotFoundException("Milestone not found");
        }
        progressHistoryToUpdate.setMilestoneId(progressHistory.getMilestoneId());
        progressHistoryToUpdate.setValue(progressHistory.getValue());

        ProgressHistory progLog = progressHistoryRepository.save(progressHistoryToUpdate);
        Double milestoneProgressRate = (progressHistory.getValue().divide(progressHistory.getMilestoneId().getTargetValue())).doubleValue();
        int numMilestones = milestoneRepository.findMilestonesByProgramId(progressHistory.getProgramId()).size();
        Double programProgressRate = progLog.getProgramId().getCurrentProgressRate() + (milestoneProgressRate/numMilestones);
        progLog.getProgramId().setCurrentProgressRate(programProgressRate);
        programRepository.save(progLog.getProgramId());
        return new ResponseEntity<>(progressHistory.getProgressHistoryId(), HttpStatus.OK);
    }

    public ResponseEntity<Object> deleteProgressHistoryRecord(Long progressHistoryId) throws ProgressHistoryNotFoundException {

        ProgressHistory progressHistory = progressHistoryRepository.findProgressHistoryByProgressHistoryId(progressHistoryId);
        if (progressHistory == null)
        {
            throw new ProgressHistoryNotFoundException("Progress History not found");
        }
        if (progressHistory.getProgramId() != null)
        {
            progressHistory.getProgramId().getUserList().clear();
            progressHistory.getProgramId().getMilestoneList().clear();
        }
        progressHistoryRepository.delete(progressHistory);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
