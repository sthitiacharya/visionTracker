package com.visiontracker.challengeTrackerApplication.repositories;

import com.visiontracker.challengeTrackerApplication.models.db.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
}
