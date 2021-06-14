package com.visiontracker.challengeTrackerApplication.services;

import com.visiontracker.challengeTrackerApplication.helper.UtilClass;
import com.visiontracker.challengeTrackerApplication.models.datamodels.CreateProgHistoryReq;
import com.visiontracker.challengeTrackerApplication.models.db.Milestone;
import com.visiontracker.challengeTrackerApplication.models.db.Program;
import com.visiontracker.challengeTrackerApplication.models.db.ProgressHistory;
import com.visiontracker.challengeTrackerApplication.models.db.User;
import com.visiontracker.challengeTrackerApplication.repositories.MilestoneRepository;
import com.visiontracker.challengeTrackerApplication.repositories.ProgramRepository;
import com.visiontracker.challengeTrackerApplication.repositories.ProgressHistoryRepository;
import com.visiontracker.challengeTrackerApplication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import util.exception.MilestoneNotFoundException;
import util.exception.ProgramNotFoundException;
import util.exception.ProgressHistoryNotFoundException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
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

    @Autowired
    private UserRepository userRepository;

    private UtilClass util = new UtilClass();

    public ResponseEntity<Object> createProgressHistoryRecord(CreateProgHistoryReq progressHistory) throws ProgramNotFoundException, MilestoneNotFoundException {

        System.out.println("In ProgramHistory service");

        if (progressHistory.getMilestoneId() == null) {
            throw new MilestoneNotFoundException("Milestone not found");
        }

        Milestone milestone = milestoneRepository.findMilestoneByMilestoneId(progressHistory.getMilestoneId());

        System.out.println("Milestone: " + milestone);
        System.out.println("Program: " + milestone.getProgramId());

        util.clearMilestoneLists(milestone);

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
        Milestone milestone = milestoneRepository.findMilestoneByMilestoneId(milestoneId);

        util.clearMilestoneLists(milestone);

        List<ProgressHistory> progressHistories = progressHistoryRepository.findProgressHistoriesByMilestoneId(milestone);
        for (ProgressHistory p : progressHistories)
        {
            if (p.getMilestoneId().getAssignedUser() != null) {
                util.clearUserLists(p.getMilestoneId().getAssignedUser());
            }
            util.clearProgramLists(p.getProgramId());
            util.clearUserLists(p.getProgramId().getProgramManager());
            util.clearUserLists(p.getMilestoneId().getMilestoneCreatedBy());
        }
        return new ResponseEntity<>(progressHistories, HttpStatus.OK);
    }

    public ResponseEntity<Object> retrieveProgressHistory(Long progressHistoryId) throws ProgressHistoryNotFoundException
    {
        ProgressHistory progressHistory = progressHistoryRepository.findProgressHistoryByProgressHistoryId(progressHistoryId);

        if (progressHistory == null)
        {
            throw new ProgressHistoryNotFoundException("Progress History not found");
        }
        util.clearMilestoneLists(progressHistory.getMilestoneId());
        util.clearProgramLists(progressHistory.getProgramId());
        util.clearUserLists(progressHistory.getProgramId().getProgramManager());
        util.clearUserLists(progressHistory.getMilestoneId().getMilestoneCreatedBy());
        if (progressHistory.getMilestoneId().getAssignedUser() != null) {
            util.clearUserLists(progressHistory.getMilestoneId().getAssignedUser());
        }

        return new ResponseEntity<>(progressHistory, HttpStatus.OK);
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
        util.clearProgramLists(progressHistory.getProgramId());

        if (progressHistory.getMilestoneId() == null)
        {
            throw new MilestoneNotFoundException("Milestone not found");
        }
        progressHistoryToUpdate.setMilestoneId(progressHistory.getMilestoneId());
        progressHistoryToUpdate.setValue(progressHistory.getValue());

        ProgressHistory progLog = progressHistoryRepository.save(progressHistoryToUpdate);
        //System.out.println("Prog Log: " + progLog + " Milestone: " + progLog.getMilestoneId() + " Program: " + progLog.getProgramId());
        calcProgressRate(progLog.getMilestoneId(), progLog.getProgramId(), progLog);

        progLog.getMilestoneId().getProgressHistories().add(progLog);
        milestoneRepository.save(progLog.getMilestoneId());
        //progLog.getMilestoneId().getProgressHistories().clear();
        return new ResponseEntity<>(progressHistory.getProgressHistoryId(), HttpStatus.OK);
    }

    public ResponseEntity<Object> deleteProgressHistoryRecord(Long progressHistoryId) throws ProgressHistoryNotFoundException {

        ProgressHistory progressHistory = progressHistoryRepository.findProgressHistoryByProgressHistoryId(progressHistoryId);
        if (progressHistory == null)
        {
            throw new ProgressHistoryNotFoundException("Progress History not found");
        }
        Program program = progressHistory.getProgramId();
        Milestone milestone = progressHistory.getMilestoneId();
        List<ProgressHistory> progressHistories = progressHistoryRepository.findProgressHistoriesByMilestoneId(milestone);
        ProgressHistory lastProgressHistory = progressHistories.get(progressHistories.size()-1);
        //int initialNumMilestones = milestoneRepository.findMilestonesByProgramId(progressHistory.getProgramId().getProgramId()).size();
        if (program != null)
        {
            util.clearProgramLists(program);
        }
        progressHistoryRepository.delete(progressHistory);
        calcProgressRate(milestone, program, lastProgressHistory);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void calcProgressRate(Milestone milestone, Program program, ProgressHistory progressHistory)
    {
        BigDecimal milestoneDifference = milestone.getTargetValue().subtract(milestone.getInitialValue());
        BigDecimal currentDifference = milestone.getTargetValue().subtract(progressHistory.getValue());
        BigDecimal change = milestoneDifference.subtract(currentDifference);
        Double milestoneProgressRate = (change.divide(milestoneDifference, 2, RoundingMode.CEILING)).doubleValue();
        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
        String date1 = f.format(progressHistory.getDateOfRecord());
        String date2 = f.format(milestone.getTargetCompletionDate());
        //System.out.println("Date of Record: " + date1 + " Milestone Target Date: " + date2);
        if (milestoneProgressRate.equals(1.00) && (date1.equals(date2) || progressHistory.getDateOfRecord().before(milestone.getTargetCompletionDate()))) {
            milestone.setActualCompletedDate(new Date());
            milestoneRepository.save(milestone);
            if (milestone.getMilestoneType().equals("Individual") && milestone.getAssignedUser() != null)
            {
                int newRewardPoints = milestone.getAssignedUser().getRewardPoints() + milestone.getRewardValue();
                milestone.getAssignedUser().setRewardPoints(newRewardPoints);
                userRepository.save(milestone.getAssignedUser());
            }
            if (milestone.getMilestoneType().equals("Program"))
            {
                List<User> users = userRepository.findUsersByProgramId(program);
                for (User u : users)
                {
                    int newRewardPoints = u.getRewardPoints() + milestone.getRewardValue();
                    u.setRewardPoints(newRewardPoints);
                    userRepository.save(u);
                }
            }
        }
        if (!milestoneProgressRate.equals(1.00) && milestone.getActualCompletedDate() != null)
        {
            milestone.setActualCompletedDate(null);
            milestoneRepository.save(milestone);
        }

        List<Milestone> milestones = milestoneRepository.findMilestonesByProgramId(program.getProgramId());
        int numMilestones = milestones.size();
        if (numMilestones == 0) { program.setCurrentProgressRate(0.00); }
        double programProgressRate = 0.00;
        for (Milestone m : milestones)
        {
            if (m != milestone)
            {
                List<ProgressHistory> progressHistories = progressHistoryRepository.findProgressHistoriesByMilestoneId(m);
                int size = progressHistories.size();
                BigDecimal milestoneDiff = m.getTargetValue().subtract(m.getInitialValue());
                if (size != 0)
                {
                    BigDecimal currentDiff = m.getTargetValue().subtract(progressHistories.get(progressHistories.size()-1).getValue());
                    BigDecimal diff = milestoneDiff.subtract(currentDiff);
                    double milestoneProgress = (diff.divide(milestoneDiff, 2, RoundingMode.CEILING)).doubleValue();
                    programProgressRate += milestoneProgress;
                }
            }
        }
        programProgressRate += milestoneProgressRate;
        programProgressRate = (programProgressRate/numMilestones) * 100;
        program.setCurrentProgressRate(programProgressRate);
        if (programProgressRate >= 100.00)
        {
            program.setCurrentProgressRate(100.00);
            program.setActualCompletedDate(new Date());
        }
        programRepository.save(program);
    }
}
