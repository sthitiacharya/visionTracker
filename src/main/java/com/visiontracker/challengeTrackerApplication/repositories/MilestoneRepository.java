package com.visiontracker.challengeTrackerApplication.repositories;

import com.visiontracker.challengeTrackerApplication.models.db.Milestone;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:4200")
public interface MilestoneRepository extends CrudRepository<Milestone, Integer> {

}
