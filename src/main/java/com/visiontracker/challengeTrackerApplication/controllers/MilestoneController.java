package com.visiontracker.challengeTrackerApplication.controllers;

import com.visiontracker.challengeTrackerApplication.models.datamodels.CreateMilestoneReq;
import com.visiontracker.challengeTrackerApplication.models.db.Milestone;
import com.visiontracker.challengeTrackerApplication.repositories.MilestoneRepository;
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

    @PostMapping(path = "/createMilestone")
    public ResponseEntity<String> createMilestone(CreateMilestoneReq createMilestoneReq)
    {
        try
        {
            if(createMilestoneReq == null) {
                return new ResponseEntity<>("Invalid create new product request", HttpStatus.BAD_REQUEST);
            }
            System.out.println("In createMilestone RESTful Web Service");
            Date creationDate = new Date();
            createMilestoneReq.getMilestone().setCreationDate(creationDate);
            Date date = new SimpleDateFormat("dd-MM-yyyy").parse(createMilestoneReq.getTargetCompletionDate());
            createMilestoneReq.getMilestone().setTargetCompletionDate(date);

            if (createMilestoneReq.getProgramId() == null)
            {
                return new ResponseEntity<>("Milestones need to be associated with a program", HttpStatus.BAD_REQUEST);
            }
            Milestone newMilestone = milestoneRepository.save(createMilestoneReq.getMilestone());

            System.out.println("********** MilestoneController.createMilestone(): Milestone " + newMilestone.getMilestoneId() + " details passed in");

            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        catch(PersistenceException ex)
        {
            if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                return new ResponseEntity<>("Milestone Title Already Exists", HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>("Unknown Persistence Exception", HttpStatus.BAD_REQUEST);
            }
        }
        catch(Exception ex)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
