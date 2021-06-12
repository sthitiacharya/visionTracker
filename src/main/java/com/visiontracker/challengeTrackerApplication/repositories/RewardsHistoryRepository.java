package com.visiontracker.challengeTrackerApplication.repositories;

import com.visiontracker.challengeTrackerApplication.models.db.Reward;
import com.visiontracker.challengeTrackerApplication.models.db.RewardsHistory;
import com.visiontracker.challengeTrackerApplication.models.db.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RewardsHistoryRepository extends CrudRepository<RewardsHistory, Long> {

    List<RewardsHistory> findRewardsHistoriesByUser(User user);
}
