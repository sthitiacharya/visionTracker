package com.visiontracker.challengeTrackerApplication.controllers;

import com.visiontracker.challengeTrackerApplication.models.datamodels.CreateMilestoneReq;
import com.visiontracker.challengeTrackerApplication.models.db.Milestone;
import com.visiontracker.challengeTrackerApplication.models.db.Program;
import com.visiontracker.challengeTrackerApplication.repositories.MilestoneRepository;
import com.visiontracker.challengeTrackerApplication.repositories.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PersistenceException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path="/Milestone")
public class MilestoneController {
    @Autowired
    private MilestoneRepository milestoneRepository;

    @Autowired
    private ProgramRepository programRepository;

    @PostMapping(path = "/createMilestone")
    public ResponseEntity<Object> createMilestone(@RequestBody CreateMilestoneReq createMilestoneReq)
    {
        try
        {
            if(createMilestoneReq == null) {
                System.out.println("Invalid create new product request");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            System.out.println("In createMilestone RESTful Web Service");
            Date creationDate = new Date();
            createMilestoneReq.getMilestone().setCreationDate(creationDate);
            Date date = new SimpleDateFormat("dd-MM-yyyy").parse(createMilestoneReq.getTargetCompletionDate());
            createMilestoneReq.getMilestone().setTargetCompletionDate(date);

            if (createMilestoneReq.getProgramId() == null)
            {
                System.out.println("Milestones need to be associated with a program");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Program program = programRepository.findProgramById(createMilestoneReq.getProgramId());
            createMilestoneReq.getMilestone().setProgramId(program);
            program.getMilestoneList().add(createMilestoneReq.getMilestone());

            Milestone newMilestone = milestoneRepository.save(createMilestoneReq.getMilestone());

            System.out.println("********** MilestoneController.createMilestone(): Milestone " + newMilestone.getMilestoneId() + " details passed in");

            return new ResponseEntity<>(newMilestone.getMilestoneId(), HttpStatus.OK);
        }
        catch (DataIntegrityViolationException ex)
        {
            System.out.println("Milestone Title Already Exists");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Duplicate Milestone Title");
        }
        catch(PersistenceException ex)
        {
            System.out.println("Unknown Persistence Exception");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
            //"Unknown Persistence Exception"
        }
        catch(Exception ex)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/getProgramMilestones")
    public ResponseEntity<Object> retrieveProgramMilestones(@RequestParam(name = "programId") Long programId)
    {
        try
        {
            Program p = programRepository.findProgramById(programId);
            if (p == null)
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Program not found");
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
}
