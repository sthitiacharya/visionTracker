package com.visiontracker.challengeTrackerApplication.services;

import com.visiontracker.challengeTrackerApplication.models.datamodels.CreateProgHistoryReq;
import com.visiontracker.challengeTrackerApplication.models.db.Milestone;
import com.visiontracker.challengeTrackerApplication.models.db.Program;
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
import java.math.RoundingMode;
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

    public ResponseEntity<Object> createProgressHistoryRecord(CreateProgHistoryReq progressHistory) throws ProgramNotFoundException, MilestoneNotFoundException {

        System.out.println("In ProgramHistory service");

        if (progressHistory.getMilestoneId() == null) {
            throw new MilestoneNotFoundException("Milestone not found");
        }

        Milestone milestone = milestoneRepository.findMilestoneByMilestoneId(progressHistory.getMilestoneId());

        System.out.println("Milestone: " + milestone);
        System.out.println("Program: " + milestone.getProgramId());
        if (!milestone.getProgressHistories().isEmpty())
        {
            milestone.getProgressHistories().clear();
        }

        progressHistory.getProgressHistory().setMilestoneId(milestone);
        progressHistory.getProgressHistory().setDateOfRecord(new Date());
        progressHistory.getProgressHistory().setProgramId(milestone.getProgramId());
        ProgressHistory newProgLog = progressHistoryRepository.save(progressHistory.getProgressHistory());

        System.out.println("Prog Log: " + newProgLog);

        calcProgressRate(milestone, milestone.getProgramId(), newProgLog);

        milestone.getProgressHistories().add(newProgLog);
        milestoneRepository.save(milestone);
        return new ResponseEntity<>(newProgLog.getProgressHistoryId(), HttpStatus.OK);
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
        System.out.println("Prog Log: " + progLog + " Milestone: " + progLog.getMilestoneId() + " Program: " + progLog.getProgramId());
        calcProgressRate(progLog.getMilestoneId(), progLog.getProgramId(), progLog);

        progLog.getMilestoneId().getProgressHistories().add(progLog);
        milestoneRepository.save(progLog.getMilestoneId());
        return new ResponseEntity<>(progressHistory.getProgressHistoryId(), HttpStatus.OK);
    }

    public ResponseEntity<Object> deleteProgressHistoryRecord(Long progressHistoryId) throws ProgressHistoryNotFoundException {

        ProgressHistory progressHistory = progressHistoryRepository.findProgressHistoryByProgressHistoryId(progressHistoryId);
        if (progressHistory == null)
        {
            throw new ProgressHistoryNotFoundException("Progress History not found");
        }
        Program program = progressHistory.getProgramId();
        int initialNumMilestones = milestoneRepository.findMilestonesByProgramId(progressHistory.getProgramId().getProgramId()).size();
        if (program != null)
        {
            program.getUserList().clear();
            program.getMilestoneList().clear();
        }
        progressHistoryRepository.delete(progressHistory);

        int numMilestones = milestoneRepository.findMilestonesByProgramId(progressHistory.getProgramId().getProgramId()).size();
        Double programProgressRate = (program.getCurrentProgressRate()/initialNumMilestones)*numMilestones;
        program.setCurrentProgressRate(programProgressRate);
        programRepository.save(program);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void calcProgressRate(Milestone milestone, Program program, ProgressHistory progressHistory)
    {
        System.out.println(milestone);
        System.out.println(program);
        System.out.println(progressHistory);
        BigDecimal milestoneDifference = milestone.getTargetValue().subtract(milestone.getInitialValue());
        BigDecimal currentDifference = milestone.getTargetValue().subtract(progressHistory.getValue());
        BigDecimal change = milestoneDifference.subtract(currentDifference);
        Double milestoneProgressRate = (change.divide(milestoneDifference, 2, RoundingMode.CEILING)).doubleValue();
        int numMilestones = milestoneRepository.findMilestonesByProgramId(program.getProgramId()).size();
        Double programProgressRate = ((program.getCurrentProgressRate() / 100) + milestoneProgressRate/numMilestones) * 100;
        program.setCurrentProgressRate(programProgressRate);
        programRepository.save(program);
    }
}
