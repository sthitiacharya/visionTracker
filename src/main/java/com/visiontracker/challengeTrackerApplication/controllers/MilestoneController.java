package com.visiontracker.challengeTrackerApplication.controllers;

import com.visiontracker.challengeTrackerApplication.models.datamodels.CreateMilestoneReq;
import com.visiontracker.challengeTrackerApplication.models.db.Milestone;
import com.visiontracker.challengeTrackerApplication.models.db.Program;
import com.visiontracker.challengeTrackerApplication.repositories.MilestoneRepository;
import com.visiontracker.challengeTrackerApplication.repositories.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.PersistenceException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping(path="/Milestone")
public class MilestoneController {
    @Autowired
    private MilestoneRepository milestoneRepository;

    @Autowired
    private ProgramRepository programRepository;

    @PostMapping(path = "/createMilestone")
    public ResponseEntity<Milestone> createMilestone(CreateMilestoneReq createMilestoneReq)
    {
        try
        {
            if(createMilestoneReq == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                //"Invalid create new product request"
            }
            System.out.println("In createMilestone RESTful Web Service");
            Date creationDate = new Date();
            createMilestoneReq.getMilestone().setCreationDate(creationDate);
            Date date = new SimpleDateFormat("dd-MM-yyyy").parse(createMilestoneReq.getTargetCompletionDate());
            createMilestoneReq.getMilestone().setTargetCompletionDate(date);

            if (createMilestoneReq.getProgramId() == null)
            {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                //"Milestones need to be associated with a program"
            }

            Milestone newMilestone = milestoneRepository.save(createMilestoneReq.getMilestone());
            Program program = programRepository.findProgramById(createMilestoneReq.getProgramId());
            newMilestone.setProgramId(program);
            program.getMilestoneList().add(newMilestone);
            System.out.println("********** MilestoneController.createMilestone(): Milestone " + newMilestone.getMilestoneId() + " details passed in");

            return new ResponseEntity<>(newMilestone, HttpStatus.ACCEPTED);
        }
        catch(PersistenceException ex)
        {
            if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                //"Milestone Title Already Exists"
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                //"Unknown Persistence Exception"
            }
        }
        catch(Exception ex)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
