package com.visiontracker.challengeTrackerApplication.repositories;

import com.visiontracker.challengeTrackerApplication.models.db.Program;
import org.springframework.data.repository.CrudRepository;

public interface ProgramRepository extends CrudRepository<Program, Integer> {
}
