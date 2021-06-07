package com.visiontracker.challengeTrackerApplication.services;

import com.visiontracker.challengeTrackerApplication.models.datamodels.CreateProgramReq;
import com.visiontracker.challengeTrackerApplication.models.datamodels.UpdateProgramReq;
import com.visiontracker.challengeTrackerApplication.models.db.Milestone;
import com.visiontracker.challengeTrackerApplication.models.db.Program;
import com.visiontracker.challengeTrackerApplication.models.db.ProgramMember;
import com.visiontracker.challengeTrackerApplication.models.db.User;
import com.visiontracker.challengeTrackerApplication.repositories.MilestoneRepository;
import com.visiontracker.challengeTrackerApplication.repositories.ProgramMemberRepository;
import com.visiontracker.challengeTrackerApplication.repositories.ProgramRepository;
import com.visiontracker.challengeTrackerApplication.repositories.UserRepository;
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
public class ProgramService {
    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MilestoneRepository milestoneRepository;

    @Autowired
    private ProgramMemberRepository programMemberRepository;

    private List<Program> enrolledPrograms;

    public ResponseEntity<Object> createProgram(CreateProgramReq createProgramReq) throws CreateNewProgramException, ProgramTitleExistException
    {
        try
        {
            if(createProgramReq == null) {
                throw new CreateNewProgramException("Invalid Create Program Request");
            }

            if (createProgramReq.getUserId() == null)
            {
                throw new CreateNewProgramException("Program must be assigned to a program manager");
            }

            if (programRepository.findProgramByTitle(createProgramReq.getProgram().getTitle()) != null)
            {
                throw new ProgramTitleExistException("Duplicate Program Title");
            }

            User programManager = userRepository.findUserById(createProgramReq.getUserId());

            if (programManager == null)
            {
                throw new CreateNewProgramException("Program Manager not found");
            }

            clearLists(programManager);

            createProgramReq.getProgram().setProgramManager(programManager);
            programManager.getProgramsManaging().add(createProgramReq.getProgram());

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
                //clearLists(user);
                createProgramReq.getProgram().getUserList().add(user);
            }

            Program newProgram = programRepository.save(createProgramReq.getProgram());
            for (Long u : createProgramReq.getUserIds())
            {
                User user = userRepository.findUserById(u);
                user.getEnrolledPrograms().add(newProgram);
                userRepository.save(user);
                ProgramMember pm = new ProgramMember(user, newProgram);
                programMemberRepository.save(pm);
            }

            //System.out.println(newProgram);
            /*
            for (User u : newProgram.getUserList())
            {
                clearLists(u);
            }
            newProgram.getUserList().clear();
            newProgram.getMilestoneList().clear();
             */
            return new ResponseEntity<>(newProgram.getProgramId(), HttpStatus.OK);
        }
        catch (ParseException ex) {
            System.out.println("Date parsed incorrectly");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Date parsed incorrectly");
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

    public ResponseEntity<Object> getEnrolledPrograms(Long userId) throws UserNotFoundException {
        User user = userRepository.findUserById(userId);
        if (user == null)
        {
            throw new UserNotFoundException("This user does not exist");
        }
        clearLists(user);

        List<Program> programs = programRepository.findProgramsByUserId(user);

        for (Program p : programs)
        {
            if (!p.getUserList().isEmpty())
            {
                p.getUserList().clear();
            }
            if (!p.getMilestoneList().isEmpty())
            {
                p.getMilestoneList().clear();
            }
        }

        /*
        for (Program program : enrolledPrograms)
        {
            if (!program.getUserList().isEmpty())
            {
                program.getUserList().clear();
            }
            if (!program.getMilestoneList().isEmpty())
            {
                program.getMilestoneList().clear();
            }
        }
         */
        System.out.println(programs);
        return new ResponseEntity<>(programs, HttpStatus.OK);
    }

    public ResponseEntity<Object> retrieveProgram(Long programId) throws ProgramNotFoundException
    {
        try
        {
            Program program = programRepository.findProgramById(programId);

            if (program == null)
            {
                throw new ProgramNotFoundException("Program not found");
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

    public ResponseEntity<Object> editProgram(UpdateProgramReq updateProgramReq) throws UpdateProgramException, ProgramTitleExistException, UserNotFoundException
    {
        try
        {
            if(updateProgramReq == null) {
                throw new UpdateProgramException("Invalid Update Program Request");
            }

            if (updateProgramReq.getProgram().getProgramId() == null)
            {
                throw new UpdateProgramException("Program ID not provided for program to be updated");
            }

            Program programToUpdate = programRepository.findProgramById(updateProgramReq.getProgram().getProgramId());

            List<ProgramMember> programMembers = programMemberRepository.findProgramMembersByProgramId(programToUpdate);
            for (ProgramMember pm : programMembers)
            {
                programMemberRepository.delete(pm);
            }
            User user = userRepository.findUserById(updateProgramReq.getUserLoggedIn());
            if (!programToUpdate.getProgramManager().equals(user))
            {
                throw new UpdateProgramException("Program can only be updated by the program manager");
            }

            Program program = programRepository.findProgramByTitle(updateProgramReq.getProgram().getTitle());
            if (program != null && !program.getProgramId().equals(programToUpdate.getProgramId()))
            {
                throw new ProgramTitleExistException("Duplicate Program Title");
            }

            programToUpdate.setTitle(updateProgramReq.getProgram().getTitle());
            programToUpdate.setDescription(updateProgramReq.getProgram().getDescription());

            User programManager = userRepository.findUserById(updateProgramReq.getUserId());

            if (programManager == null)
            {
                throw new UserNotFoundException("Program Manager not found");
            }

            clearLists(programManager);

            programToUpdate.setProgramManager(programManager);
            programManager.getProgramsManaging().add(programToUpdate);

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

                //user.getEnrolledPrograms().add(programToUpdate);
            }

            Program updatedProgram = programRepository.save(programToUpdate);
            System.out.println(updatedProgram);

            for (Long u : updateProgramReq.getUserIds())
            {
                User user1 = userRepository.findUserById(u);
                user1.getEnrolledPrograms().add(updatedProgram);
                userRepository.save(user);
                ProgramMember pm = new ProgramMember(user, updatedProgram);
                programMemberRepository.save(pm);

            }
            /*
            for (User u : updatedProgram.getUserList())
            {
                u.getEnrolledPrograms().clear();
                u.getMilestonesCreated().clear();
                u.getProgramsManaging().clear();
                u.getMilestoneList().clear();
            }
            updatedProgram.getUserList().clear();
            updatedProgram.getMilestoneList().clear();
             */
            return new ResponseEntity<>(updatedProgram.getProgramId(), HttpStatus.OK);
        }
        catch (ParseException ex)
        {
            System.out.println("Date parsed incorrectly");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Date parsed incorrectly");
        }
    }

    public ResponseEntity<Object> deleteProgram(Long programId) throws ProgramNotFoundException {
        Program programToDelete = programRepository.findProgramById(programId);

        if (programToDelete == null)
        {
            throw new ProgramNotFoundException("Program not found");
        }

        List<Milestone> milestones = milestoneRepository.findMilestonesByProgramId(programToDelete);

        for (Milestone m : milestones)
        {
            milestoneRepository.delete(m);
        }

        programRepository.delete(programToDelete);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public List<Program> getEnrolledPrograms() {
        return enrolledPrograms;
    }

    public void setEnrolledPrograms(List<Program> enrolledPrograms) {
        this.enrolledPrograms = enrolledPrograms;
    }
}
