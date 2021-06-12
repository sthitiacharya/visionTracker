package com.visiontracker.challengeTrackerApplication.services;

import com.visiontracker.challengeTrackerApplication.helper.UtilClass;
import com.visiontracker.challengeTrackerApplication.models.datamodels.CreateMilestoneReq;
import com.visiontracker.challengeTrackerApplication.models.datamodels.UpdateMilestoneReq;
import com.visiontracker.challengeTrackerApplication.models.db.Milestone;
import com.visiontracker.challengeTrackerApplication.models.db.Program;
import com.visiontracker.challengeTrackerApplication.models.db.User;
import com.visiontracker.challengeTrackerApplication.repositories.MilestoneRepository;
import com.visiontracker.challengeTrackerApplication.repositories.ProgramRepository;
import com.visiontracker.challengeTrackerApplication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import util.exception.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class MilestoneService {
    @Autowired
    private MilestoneRepository milestoneRepository;

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private UserRepository userRepository;

    public UtilClass util = new UtilClass();

    public ResponseEntity<Object> createMilestone(CreateMilestoneReq createMilestoneReq) throws CreateNewMilestoneException, MilestoneTitleExistException, ProgramNotFoundException, ParseException
    {
        if(createMilestoneReq == null) {
            throw new CreateNewMilestoneException("Invalid create new product request");
        }

        if (createMilestoneReq.getProgramId() == null)
        {
            throw new CreateNewMilestoneException("Milestones need to be associated with a program");
        }

        if (milestoneRepository.findMilestoneByTitle(createMilestoneReq.getMilestone().getTitle()) != null)
        {
            throw new MilestoneTitleExistException("Duplicate Milestone Title");
        }

        Date creationDate = new Date();
        createMilestoneReq.getMilestone().setCreationDate(creationDate);

        Program program = programRepository.findProgramByProgramId(createMilestoneReq.getProgramId());

        if (program == null)
        {
            throw new ProgramNotFoundException("Program Not Found");
        }
        createMilestoneReq.getMilestone().setProgramId(program);
        util.clearProgramLists(program);

        Milestone newMilestone = milestoneRepository.save(createMilestoneReq.getMilestone());
        program.getMilestoneList().add(newMilestone);

        return new ResponseEntity<>(newMilestone.getMilestoneId(), HttpStatus.OK);

    }

    public ResponseEntity<Object> retrieveProgramMilestones(Long programId)
    {
        List<Milestone> milestones = milestoneRepository.findMilestonesByProgramId(programId);
        System.out.println("Program Milestones: " + milestones);

        for (Milestone m : milestones)
        {
            util.clearMilestoneLists(m);
            util.clearProgramLists(m.getProgramId());
            util.clearUserLists(m.getMilestoneCreatedBy());
            util.clearUserLists(m.getProgramId().getProgramManager());
        }

        return new ResponseEntity<>(milestones, HttpStatus.OK);
    }

    public ResponseEntity<Object> retrieveMilestone(Long milestoneId) throws MilestoneNotFoundException
    {
        try
        {
            Milestone milestone = milestoneRepository.findMilestoneByMilestoneId(milestoneId);

            if (milestone == null)
            {
                throw new MilestoneNotFoundException("Milestone not found");
            }

            util.clearMilestoneLists(milestone);
            if (milestone.getProgramId() != null)
            {
                util.clearProgramLists(milestone.getProgramId());
                util.clearUserLists(milestone.getProgramId().getProgramManager());
            }
            if (milestone.getMilestoneCreatedBy() != null)
            {
                util.clearUserLists(milestone.getMilestoneCreatedBy());
            }

            return new ResponseEntity<>(milestone, HttpStatus.OK);
        }
        catch (Exception ex)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error");
        }
    }

    public ResponseEntity<Object> editMilestone(Long milestoneId, UpdateMilestoneReq updateMilestoneReq) throws UpdateMilestoneException, MilestoneTitleExistException, ParseException
    {
        if(updateMilestoneReq == null) {
            throw new UpdateMilestoneException("Invalid update milestone request");
        }

        if (updateMilestoneReq.getMilestone() == null || updateMilestoneReq.getMilestone().getMilestoneId() == null)
        {
            throw new UpdateMilestoneException("Milestone ID not provided for milestone to be updated");
        }
        Milestone milestoneToUpdate = milestoneRepository.findMilestoneByMilestoneId(updateMilestoneReq.getMilestone().getMilestoneId());

        if (updateMilestoneReq.getProgramId() == null)
        {
            throw new UpdateMilestoneException("Milestones need to be associated with a program");
        }
        Program program = programRepository.findProgramByProgramId(updateMilestoneReq.getProgramId());
        milestoneToUpdate.setProgramId(program);

        util.clearProgramLists(program);

        Milestone milestone = milestoneRepository.findMilestoneByTitle(updateMilestoneReq.getMilestone().getTitle());
        if (milestone != null && !milestone.getMilestoneId().equals(milestoneToUpdate.getMilestoneId()))
        {
            throw new MilestoneTitleExistException("Duplicate Milestone Title");
        }
        milestoneToUpdate.setTitle(updateMilestoneReq.getMilestone().getTitle());
        milestoneToUpdate.setDescription(updateMilestoneReq.getMilestone().getDescription());
        milestoneToUpdate.setMilestoneType(updateMilestoneReq.getMilestone().getMilestoneType());
        milestoneToUpdate.setInitialValue(updateMilestoneReq.getMilestone().getInitialValue());
        milestoneToUpdate.setTargetValue(updateMilestoneReq.getMilestone().getTargetValue());
        milestoneToUpdate.setRewardValue(updateMilestoneReq.getMilestone().getRewardValue());
        milestoneToUpdate.setValueCategory(updateMilestoneReq.getMilestone().getValueCategory());
        milestoneToUpdate.setValueType(updateMilestoneReq.getMilestone().getValueType());
        milestoneToUpdate.setTargetCompletionDate(updateMilestoneReq.getMilestone().getTargetCompletionDate());
        milestoneToUpdate.setReminderStartDate(updateMilestoneReq.getMilestone().getReminderStartDate());
        milestoneToUpdate.setReminderInterval(updateMilestoneReq.getMilestone().getReminderInterval());

        Milestone updatedMilestone = milestoneRepository.save(milestoneToUpdate);
        program.getMilestoneList().add(updatedMilestone);

        System.out.println("********** MilestoneController.editMilestone(): Milestone " + updatedMilestone.getMilestoneId() + " details updated");

        return new ResponseEntity<>(updatedMilestone.getMilestoneId(), HttpStatus.OK);
    }

    public ResponseEntity<Object> deleteMilestone(Long milestoneId) throws MilestoneNotFoundException {
        Milestone milestoneToDelete = milestoneRepository.findMilestoneByMilestoneId(milestoneId);

        if (milestoneToDelete == null)
        {
            throw new MilestoneNotFoundException("Milestone not found");
        }

        milestoneRepository.delete(milestoneToDelete);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<Object> getReminders(Long userId) {
        User user = userRepository.findUserById(userId);
        util.clearUserLists(user);
        List<Program> programs = programRepository.findProgramsByUserId(user);
        List<Milestone> milestones = new ArrayList<>();
        List<String> reminders = new ArrayList<>();
        for (Program p : programs)
        {
            List<Milestone> progMilestones = milestoneRepository.findMilestonesByProgramId(p.getProgramId());
            milestones.addAll(progMilestones);
            util.clearProgramLists(p);
        }

        for (Milestone m : milestones)
        {
            if (m.getReminderStartDate() == null)
            {
                break;
            }
            SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
            if (f.format(m.getReminderStartDate()).equals(f.format(new Date())))
            {
                if (f.format(m.getReminderStartDate()).compareTo(f.format(m.getTargetCompletionDate())) == 0)
                {
                    String reminder = "Reminder: " + m.getTitle() + " is due today";
                    reminders.add(reminder);
                }

                String reminder = "Reminder to complete " + m.getTitle() + " by: " + m.getTargetCompletionDate();
                reminders.add(reminder);

                if (m.getReminderInterval().equals("Daily"))
                {
                    Calendar c = Calendar.getInstance();
                    c.setTime(m.getReminderStartDate());
                    c.add(Calendar.DAY_OF_MONTH, 1);
                    System.out.println(c.getTime());
                    m.setReminderStartDate(c.getTime());
                }
                if (m.getReminderInterval().equals("Every 3 days"))
                {
                    Calendar c = Calendar.getInstance();
                    c.setTime(m.getReminderStartDate());
                    c.add(Calendar.DAY_OF_MONTH, 3);
                    System.out.println(c.getTime());
                    m.setReminderStartDate(c.getTime());
                }
                if (m.getReminderInterval().equals("Every 5 days"))
                {
                    Calendar c = Calendar.getInstance();
                    c.setTime(m.getReminderStartDate());
                    c.add(Calendar.DAY_OF_MONTH, 5);
                    System.out.println(c.getTime());
                    m.setReminderStartDate(c.getTime());
                }
                if (m.getReminderInterval().equals("Weekly"))
                {
                    Calendar c = Calendar.getInstance();
                    c.setTime(m.getReminderStartDate());
                    c.add(Calendar.DAY_OF_MONTH, 7);
                    System.out.println(c.getTime());
                    m.setReminderStartDate(c.getTime());
                }
                milestoneRepository.save(m);
            }
            if (f.format(m.getReminderStartDate()).compareTo(f.format(m.getTargetCompletionDate())) > 0)
            {
                m.setReminderStartDate(m.getTargetCompletionDate());
                milestoneRepository.save(m);
            }
            util.clearProgramLists(m.getProgramId());
            util.clearMilestoneLists(m);
            util.clearUserLists(m.getMilestoneCreatedBy());
            util.clearUserLists(m.getProgramId().getProgramManager());
        }

        return new ResponseEntity<>(reminders, HttpStatus.OK);
    }
}
