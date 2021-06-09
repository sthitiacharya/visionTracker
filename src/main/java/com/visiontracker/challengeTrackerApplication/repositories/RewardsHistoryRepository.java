package com.visiontracker.challengeTrackerApplication.repositories;

import com.visiontracker.challengeTrackerApplication.models.db.RewardsHistory;
import org.springframework.data.repository.CrudRepository;

public interface RewardsHistoryRepository extends CrudRepository<RewardsHistory, Long> {
}
