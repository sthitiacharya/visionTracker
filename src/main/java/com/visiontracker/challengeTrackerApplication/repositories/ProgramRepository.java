package com.visiontracker.challengeTrackerApplication.repositories;

import com.visiontracker.challengeTrackerApplication.models.db.Program;
import com.visiontracker.challengeTrackerApplication.models.db.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
public interface ProgramRepository extends CrudRepository<Program, Long> {

    Program findProgramByProgramId(Long programId);

    Program findProgramByTitle(String title);

    List<Program> findAll();

    @Query("select distinct pm.programId from ProgramMember pm where pm.userId = :userId")
    List<Program> findProgramsByUserId(@Param("userId") User userId);
}
