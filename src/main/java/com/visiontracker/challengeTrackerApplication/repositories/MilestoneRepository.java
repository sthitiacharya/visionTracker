package com.visiontracker.challengeTrackerApplication.repositories;

import com.visiontracker.challengeTrackerApplication.models.db.Milestone;
import org.springframework.data.repository.CrudRepository;

public interface MilestoneRepository extends CrudRepository<Milestone, Integer> {

}
