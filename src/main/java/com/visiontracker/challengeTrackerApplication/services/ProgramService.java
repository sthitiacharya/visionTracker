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

        createProgramReq.getProgram().setProgramManager(programManager);

        createProgramReq.getProgram().setCurrentProgressRate(0.00);

        if (!createProgramReq.getUserIds().contains(createProgramReq.getUserId()))
        {
            createProgramReq.getUserIds().add(createProgramReq.getUserId());
        }

        for (Long u : createProgramReq.getUserIds())
        {
            User user = userRepository.findUserById(u);
            createProgramReq.getProgram().getUserList().add(user);
        }

        Program newProgram = programRepository.save(createProgramReq.getProgram());
        programManager.getProgramsManaging().add(newProgram);
        userRepository.save(programManager);

        for (User u : newProgram.getUserList())
        {
            u.getEnrolledPrograms().add(newProgram);
            userRepository.save(u);
            ProgramMember pm = new ProgramMember();
            pm.setProgramId(newProgram);
            pm.setUserId(u);
            programMemberRepository.save(pm);
        }

        return new ResponseEntity<>(newProgram.getProgramId(), HttpStatus.OK);
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
            clearLists(p.getProgramManager());
        }

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
        if(updateProgramReq == null) {
            throw new UpdateProgramException("Invalid Update Program Request");
        }

        if (updateProgramReq.getProgram().getProgramId() == null)
        {
            throw new UpdateProgramException("Program ID not provided for program to be updated");
        }

        Program programToUpdate = programRepository.findProgramById(updateProgramReq.getProgram().getProgramId());

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
        programToUpdate.setStartDate(updateProgramReq.getProgram().getStartDate());
        programToUpdate.setTargetCompletionDate(updateProgramReq.getProgram().getTargetCompletionDate());

        User programManager = userRepository.findUserById(updateProgramReq.getUserId());

        if (programManager == null)
        {
            throw new UserNotFoundException("Program Manager not found");
        }

        programToUpdate.setProgramManager(programManager);

        if (!updateProgramReq.getUserIds().contains(updateProgramReq.getUserId()))
        {
            updateProgramReq.getUserIds().add(updateProgramReq.getUserId());
        }

        for (Long u : updateProgramReq.getUserIds())
        {
            User enrolledUser = userRepository.findUserById(u);
            updateProgramReq.getProgram().getUserList().add(enrolledUser);
        }
        programToUpdate.setUserList(updateProgramReq.getProgram().getUserList());

        List<ProgramMember> programMembers = programMemberRepository.findProgramMembersByProgramId(programToUpdate);
        for (ProgramMember pm : programMembers)
        {
            programMemberRepository.delete(pm);
        }

        Program updatedProgram = programRepository.save(programToUpdate);
        System.out.println(updatedProgram);
        programManager.getProgramsManaging().add(updatedProgram);
        for (User u : updatedProgram.getUserList())
        {
            programMemberRepository.save(new ProgramMember(u, updatedProgram));
        }
        return new ResponseEntity<>(updatedProgram.getProgramId(), HttpStatus.OK);
    }

    public ResponseEntity<Object> deleteProgram(Long programId) throws ProgramNotFoundException {
        Program programToDelete = programRepository.findProgramById(programId);

        if (programToDelete == null)
        {
            throw new ProgramNotFoundException("Program not found");
        }

        List<Milestone> milestones = milestoneRepository.findMilestonesByProgramId(programId);

        List<ProgramMember> programMembers = programMemberRepository.findProgramMembersByProgramId(programToDelete);

        for (Milestone m : milestones)
        {
            milestoneRepository.delete(m);
        }

        for (ProgramMember pm : programMembers)
        {
            programMemberRepository.delete(pm);
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
