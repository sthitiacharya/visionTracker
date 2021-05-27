package com.visiontracker.challengeTrackerApplication.repositories;

import com.visiontracker.challengeTrackerApplication.models.db.Milestone;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
public interface MilestoneRepository extends CrudRepository<Milestone, Integer> {

    @Query("select m from Milestone m where m.programId = :programId")
    public List<Milestone> findMilestonesByProgramId(@Param("programId") Integer programId);
}
