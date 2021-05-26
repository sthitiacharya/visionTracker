package com.visiontracker.challengeTrackerApplication.repositories;

import com.visiontracker.challengeTrackerApplication.models.db.Program;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:4200")
public interface ProgramRepository extends CrudRepository<Program, Integer> {

    @Query("select p from Program p where p.programId = :programId")
    public Program findProgramById(@Param("programId") Integer programId);
}
