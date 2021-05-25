package com.visiontracker.challengeTrackerApplication.controllers;

import com.visiontracker.challengeTrackerApplication.models.datamodels.CreateProgramReq;
import com.visiontracker.challengeTrackerApplication.models.db.Program;
import com.visiontracker.challengeTrackerApplication.models.db.User;
import com.visiontracker.challengeTrackerApplication.repositories.ProgramRepository;
import com.visiontracker.challengeTrackerApplication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.PersistenceException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping(path="/Program")
public class ProgramController {
    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private UserRepository userRepository;

    @PutMapping("/createProgram")
    public ResponseEntity<Program> createProgram(CreateProgramReq createProgramReq)
    {
        if(createProgramReq == null) {
            return new ResponseEntity<Program> (HttpStatus.BAD_REQUEST);
            //"Invalid Create Program Request"
        }
        try
        {
            System.out.println("In createProgram RESTful Web Service");

            Date date = new SimpleDateFormat("dd-MM-yyyy").parse(createProgramReq.getStartDate());
            createProgramReq.getProgram().setStartDate(date);

            Date date2 = new SimpleDateFormat("dd-MM-yyyy").parse(createProgramReq.getTargetCompletionDate());
            createProgramReq.getProgram().setTargetCompletionDate(date2);

            if (createProgramReq.getProgram().getProgramManager() == null)
            {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                //"Program must be assigned to a program manager"
            }

            Program newProgram = programRepository.save(createProgramReq.getProgram());
            newProgram.getProgramManager().getProgramsManaging().add(newProgram);
            for (Integer u : createProgramReq.getUserIds())
            {
                User user = userRepository.findUserById(u);
                newProgram.getUserList().add(user);
                user.getEnrolledPrograms().add(newProgram);
            }

            return new ResponseEntity<Program>(newProgram,HttpStatus.ACCEPTED);
        }
        catch (PersistenceException ex)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        catch (ParseException ex)
        {
            System.out.println("Date parsed incorrectly");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        catch(Exception ex)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
