package com.visiontracker.challengeTrackerApplication.repositories;

import com.visiontracker.challengeTrackerApplication.models.db.Reward;
import com.visiontracker.challengeTrackerApplication.models.db.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RewardRepository extends CrudRepository<Reward, Long> {

    @Query("select r from Reward r where r.rewardId = :rewardId")
    Reward findRewardByRewardId(@Param("rewardId") Long rewardId);

    @Query("select r from Reward r")
    List<Reward> findAllRewards();

    @Query("select distinct rh.reward from RewardsHistory rh where rh.user = :user")
    List<Reward> findRewardsByUser(@Param("user") User user);
}
