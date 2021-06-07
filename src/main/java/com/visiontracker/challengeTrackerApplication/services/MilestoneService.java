package com.visiontracker.challengeTrackerApplication.services;

import com.visiontracker.challengeTrackerApplication.models.datamodels.CreateMilestoneReq;
import com.visiontracker.challengeTrackerApplication.models.datamodels.UpdateMilestoneReq;
import com.visiontracker.challengeTrackerApplication.models.db.Milestone;
import com.visiontracker.challengeTrackerApplication.models.db.Program;
import com.visiontracker.challengeTrackerApplication.repositories.MilestoneRepository;
import com.visiontracker.challengeTrackerApplication.repositories.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import util.exception.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class MilestoneService {
    @Autowired
    private MilestoneRepository milestoneRepository;

    @Autowired
    private ProgramRepository programRepository;

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
        Date date = new SimpleDateFormat("dd-MM-yyyy").parse(createMilestoneReq.getTargetCompletionDate());
        createMilestoneReq.getMilestone().setTargetCompletionDate(date);

        Program program = programRepository.findProgramById(createMilestoneReq.getProgramId());

        if (program == null)
        {
            throw new ProgramNotFoundException("Program Not Found");
        }
        createMilestoneReq.getMilestone().setProgramId(program);

        if (!program.getMilestoneList().isEmpty())
        {
            program.getMilestoneList().clear();
        }

        if (!program.getUserList().isEmpty())
        {
            program.getUserList().clear();
        }

        Milestone newMilestone = milestoneRepository.save(createMilestoneReq.getMilestone());
        program.getMilestoneList().add(newMilestone);

        return new ResponseEntity<>(newMilestone.getMilestoneId(), HttpStatus.OK);

    }

    public ResponseEntity<Object> retrieveProgramMilestones(Long programId) throws ProgramNotFoundException
    {
        try
        {
            Program p = programRepository.findProgramById(programId);
            if (p == null)
            {
                throw new ProgramNotFoundException("Program not found");
            }
            List<Milestone> milestones = milestoneRepository.findMilestonesByProgramId(p);

            for (Milestone m : milestones)
            {
                m.getProgramId().getMilestoneList().clear();
                m.getProgramId().getUserList().clear();
                m.getMilestoneCreatedBy().getMilestoneList().clear();
                m.getMilestoneCreatedBy().getMilestonesCreated().clear();
                m.getMilestoneCreatedBy().getProgramsManaging().clear();
                m.getMilestoneCreatedBy().getEnrolledPrograms().clear();
                //m.getAssignedUser().getMilestonesCreated().clear();
                //m.getAssignedUser().getProgramsManaging().clear();
                //m.getAssignedUser().getEnrolledPrograms().clear();
                //m.getAssignedUser().getMilestoneList().clear();
            }
            p.getUserList().clear();
            p.getMilestoneList().clear();
            return new ResponseEntity<>(milestones, HttpStatus.OK);
        }
        catch (Exception ex)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error");
        }
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

            if (milestone.getProgramId() != null)
            {
                milestone.getProgramId().getMilestoneList().clear();
                milestone.getProgramId().getUserList().clear();
            }

            if (milestone.getMilestoneCreatedBy() != null)
            {
                milestone.getMilestoneCreatedBy().getMilestoneList().clear();
                milestone.getMilestoneCreatedBy().getMilestonesCreated().clear();
                milestone.getMilestoneCreatedBy().getProgramsManaging().clear();
                milestone.getMilestoneCreatedBy().getEnrolledPrograms().clear();
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
        Program program = programRepository.findProgramById(updateMilestoneReq.getProgramId());
        milestoneToUpdate.setProgramId(program);

        if (!program.getMilestoneList().isEmpty())
        {
            program.getMilestoneList().clear();
        }

        if (!program.getUserList().isEmpty())
        {
            program.getUserList().clear();
        }

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

        Date date = new SimpleDateFormat("dd-MM-yyyy").parse(updateMilestoneReq.getTargetCompletionDate());
        milestoneToUpdate.setTargetCompletionDate(date);

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
}
