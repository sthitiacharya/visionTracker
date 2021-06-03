package com.visiontracker.challengeTrackerApplication.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.visiontracker.challengeTrackerApplication.models.datamodels.CreateProgramReq;
import com.visiontracker.challengeTrackerApplication.models.datamodels.UpdateProgramReq;
import com.visiontracker.challengeTrackerApplication.models.db.Program;
import com.visiontracker.challengeTrackerApplication.models.db.User;
import com.visiontracker.challengeTrackerApplication.repositories.ProgramRepository;
import com.visiontracker.challengeTrackerApplication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PersistenceException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path="/Program")
public class ProgramController {
    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/createProgram")
    public ResponseEntity<Object> createProgram(@RequestBody CreateProgramReq createProgramReq)
    {
        try
        {
            if(createProgramReq == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Create Program Request");
            }

            if (createProgramReq.getUserId() == null)
            {
                System.out.println("Program must be assigned to a program manager");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Program must be assigned to a program manager");
            }

            if (programRepository.findProgramByTitle(createProgramReq.getProgram().getTitle()) != null)
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Duplicate Program Title");
            }

            User programManager = userRepository.findUserById(createProgramReq.getUserId());

            if (programManager == null)
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Program Manager not found");
            }

            clearLists(programManager);

            createProgramReq.getProgram().setProgramManager(programManager);
            programManager.getProgramsManaging().add(createProgramReq.getProgram());

            System.out.println("In createProgram RESTful Web Service");

            Date date = new SimpleDateFormat("dd-MM-yyyy").parse(createProgramReq.getStartDate());
            createProgramReq.getProgram().setStartDate(date);

            Date date2 = new SimpleDateFormat("dd-MM-yyyy").parse(createProgramReq.getTargetCompletionDate());
            createProgramReq.getProgram().setTargetCompletionDate(date2);

            if (!createProgramReq.getUserIds().contains(createProgramReq.getUserId()))
            {
                createProgramReq.getUserIds().add(createProgramReq.getUserId());
            }

            for (Long u : createProgramReq.getUserIds())
            {
                User user = userRepository.findUserById(u);
                clearLists(user);
                createProgramReq.getProgram().getUserList().add(user);
                //user.getEnrolledPrograms().add(createProgramReq.getProgram());
            }

            /*ObjectMapper mapper = new ObjectMapper();
            String content = mapper.writeValueAsString(createProgramReq.getProgram());
            System.out.println(content);*/

            Program newProgram = programRepository.save(createProgramReq.getProgram());
            System.out.println(newProgram);

            for (User u : newProgram.getUserList())
            {
                u.getEnrolledPrograms().clear();
                u.getMilestonesCreated().clear();
                u.getProgramsManaging().clear();
                u.getMilestoneList().clear();
            }
            newProgram.getUserList().clear();
            newProgram.getMilestoneList().clear();

            return new ResponseEntity<>(newProgram.getProgramId(), HttpStatus.OK);
        }
        catch (DataIntegrityViolationException ex)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Duplicate Program Title");
        }
        catch (PersistenceException ex)
        {
            System.out.println("Persistence Exception");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Persistence Exception");
        }
        catch (ParseException ex)
        {
            System.out.println("Date parsed incorrectly");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Date parsed incorrectly");
        }
        catch(IllegalArgumentException ex)
        {
            System.out.println("Illegal Argument Error");
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getClass());
        }
        catch(Exception ex)
        {
            System.out.println("Internal Server Error");
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getClass());
        }
    }

    private void clearLists(User user) {
        if (!user.getProgramsManaging().isEmpty())
        {
            user.getProgramsManaging().clear();
        }
        if (!user.getMilestoneList().isEmpty())
        {
            user.getMilestoneList().clear();
        }
        if (!user.getEnrolledPrograms().isEmpty())
        {
            user.getEnrolledPrograms().clear();
        }
        if (!user.getMilestonesCreated().isEmpty())
        {
            user.getMilestonesCreated().clear();
        }
    }

    @GetMapping(path = "/getEnrolledPrograms")
    public ResponseEntity<List<Program>> getEnrolledPrograms(@RequestParam(name = "userId") Long userId)
    {
        User user = userRepository.findUserById(userId);
        if (user == null)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        user.getMilestonesCreated().clear();
        user.getEnrolledPrograms().clear();
        user.getProgramsManaging().clear();
        user.getMilestoneList().clear();

        List<Program> programs = programRepository.findAll();
        List<Program> enrolledPrograms = new ArrayList<>();

        for (Program p : programs)
        {
            if (p.getUserList().contains(user))
            {
                enrolledPrograms.add(p);
            }
            p.getUserList().clear();
            p.getMilestoneList().clear();
        }

        return new ResponseEntity<>(programs, HttpStatus.OK);
    }

    @GetMapping(path = "getEnrolledPrograms/{programId}")
    public ResponseEntity<Object> retrieveProgram(@PathVariable(name = "programId") Long programId)
    {
        try
        {
            Program program = programRepository.findProgramById(programId);

            if (program == null)
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Program not found");
            }

            if (!program.getMilestoneList().isEmpty())
            {
                program.getMilestoneList().clear();
            }
            if (!program.getUserList().isEmpty())
            {
                program.getUserList().clear();
            }

            if (program.getProgramManager() != null)
            {
                program.getProgramManager().getEnrolledPrograms().clear();
                program.getProgramManager().getProgramsManaging().clear();
                program.getProgramManager().getMilestonesCreated().clear();
                program.getProgramManager().getMilestoneList().clear();
            }

            return new ResponseEntity<>(program, HttpStatus.OK);
        }
        catch (Exception ex)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error");
        }
    }

    @PutMapping("/editProgram")
    public ResponseEntity<Object> editProgram(@RequestBody UpdateProgramReq updateProgramReq)
    {
        try
        {
            if(updateProgramReq == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Update Program Request");
            }

            if (updateProgramReq.getProgram() == null || updateProgramReq.getProgram().getProgramId() == null)
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Program ID not provided for program to be updated");
            }

            Program programToUpdate = programRepository.findProgramById(updateProgramReq.getProgram().getProgramId());

            User user = userRepository.findUserById(updateProgramReq.getUserLoggedIn());
            if (!programToUpdate.getProgramManager().equals(user))
            {
                System.out.println("Program can only be updated by the program manager");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Program can only be updated by the program manager");
            }

            Program program = programRepository.findProgramByTitle(updateProgramReq.getProgram().getTitle());
            if (program != null && !program.getProgramId().equals(programToUpdate.getProgramId()))
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Duplicate Program Title");
            }

            programToUpdate.setTitle(updateProgramReq.getProgram().getTitle());
            programToUpdate.setDescription(updateProgramReq.getProgram().getDescription());

            User programManager = userRepository.findUserById(updateProgramReq.getUserId());

            if (programManager == null)
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Program Manager not found");
            }

            clearLists(programManager);

            programToUpdate.setProgramManager(programManager);
            programManager.getProgramsManaging().add(programToUpdate);

            System.out.println("In editProgram RESTful Web Service");

            Date date = new SimpleDateFormat("dd-MM-yyyy").parse(updateProgramReq.getStartDate());
            programToUpdate.setStartDate(date);

            Date date2 = new SimpleDateFormat("dd-MM-yyyy").parse(updateProgramReq.getTargetCompletionDate());
            programToUpdate.setTargetCompletionDate(date2);

            if (!updateProgramReq.getUserIds().contains(updateProgramReq.getUserId()))
            {
                updateProgramReq.getUserIds().add(updateProgramReq.getUserId());
            }

            for (Long u : updateProgramReq.getUserIds())
            {
                User enrolledUser = userRepository.findUserById(u);
                clearLists(enrolledUser);
                programToUpdate.getUserList().add(enrolledUser);
                user.getEnrolledPrograms().add(programToUpdate);
            }

            Program updatedProgram = programRepository.save(programToUpdate);
            System.out.println(updatedProgram);

            for (User u : updatedProgram.getUserList())
            {
                u.getEnrolledPrograms().clear();
                u.getMilestonesCreated().clear();
                u.getProgramsManaging().clear();
                u.getMilestoneList().clear();
            }
            updatedProgram.getUserList().clear();
            updatedProgram.getMilestoneList().clear();

            return new ResponseEntity<>(updatedProgram.getProgramId(), HttpStatus.OK);
        }
        catch (DataIntegrityViolationException ex)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Duplicate Program Title");
        }
        catch (PersistenceException ex)
        {
            System.out.println("Persistence Exception");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Persistence Exception");
        }
        catch (ParseException ex)
        {
            System.out.println("Date parsed incorrectly");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Date parsed incorrectly");
        }
        catch(IllegalArgumentException ex)
        {
            System.out.println("Illegal Argument Error");
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getClass());
        }
        catch(Exception ex)
        {
            System.out.println("Internal Server Error");
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getClass());
        }
    }
}
