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

    @Query("select p from Program p where p.programId = :programId")
    Program findProgramById(@Param("programId") Long programId);

    @Query("select p from Program p where p.title = :title")
    Program findProgramByTitle(@Param("title") String title);

    @Query("select p from Program p")
    List<Program> findAll();

    @Query("select distinct pm.programId from ProgramMember pm where pm.userId = :userId")
    List<Program> findProgramsByUserId(@Param("userId") User userId);
}
