package com.visiontracker.challengeTrackerApplication.services;

import com.visiontracker.challengeTrackerApplication.helper.UtilClass;
import com.visiontracker.challengeTrackerApplication.models.db.Reward;
import com.visiontracker.challengeTrackerApplication.models.db.RewardsHistory;
import com.visiontracker.challengeTrackerApplication.models.db.User;
import com.visiontracker.challengeTrackerApplication.repositories.RewardRepository;
import com.visiontracker.challengeTrackerApplication.repositories.RewardsHistoryRepository;
import com.visiontracker.challengeTrackerApplication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import util.exception.RedeemRewardException;
import util.exception.RewardNotFoundException;
import util.exception.UserNotFoundException;

import java.util.Date;
import java.util.List;

@Service
public class RewardService {
    @Autowired
    private RewardRepository rewardRepository;

    @Autowired
    private RewardsHistoryRepository rewardsHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    private UtilClass util = new UtilClass();

    public ResponseEntity<Object> viewAllRewards()
    {
        List<Reward> rewards = rewardRepository.findAll();
        return new ResponseEntity<>(rewards, HttpStatus.OK);
    }

    public ResponseEntity<Object> viewRewardDetails(Long rewardId) throws RewardNotFoundException {
        Reward reward = rewardRepository.findRewardByRewardId(rewardId);
        if (reward == null)
        {
            throw new RewardNotFoundException("Reward not found");
        }

        return new ResponseEntity<>(reward, HttpStatus.OK);
    }

    public ResponseEntity<Object> redeemReward(Long rewardId, Long userId) throws RewardNotFoundException, UserNotFoundException, RedeemRewardException {
        Reward reward = rewardRepository.findRewardByRewardId(rewardId);
        if (reward == null)
        {
            throw new RewardNotFoundException("Reward not found");
        }
        User user = userRepository.findUserById(userId);
        if (user == null)
        {
            throw new UserNotFoundException("User not found");
        }
        if (user.getRewardPoints() < reward.getRedeemablePoints())
        {
            throw new RedeemRewardException("You have insufficient reward points to redeem this reward");
        }

        RewardsHistory rewardHistory = new RewardsHistory();
        rewardHistory.setReward(reward);
        rewardHistory.setUser(user);
        rewardHistory.setDateOfRedemption(new Date());
        rewardHistory.setRedeemPointValue(reward.getRedeemablePoints());
        RewardsHistory newRewardHistory = rewardsHistoryRepository.save(rewardHistory);

        newRewardHistory.getUser().setRewardPoints(newRewardHistory.getUser().getRewardPoints() - newRewardHistory.getRedeemPointValue());
        userRepository.save(newRewardHistory.getUser());
        util.clearUserLists(newRewardHistory.getUser());

        return new ResponseEntity<>(newRewardHistory.getRewardsHistoryId(), HttpStatus.OK);
    }

    public ResponseEntity<Object> viewRedeemedRewards(Long userId) throws UserNotFoundException {
        User user = userRepository.findUserById(userId);
        if (user == null)
        {
            throw new UserNotFoundException("User not found!");
        }
        util.clearUserLists(user);
        List<Reward> redeemedRewards = rewardRepository.findRewardsByUser(user);
        //List<RewardsHistory> rewardsHistories = rewardsHistoryRepository.findRewardsHistoriesByUser(user);
        return new ResponseEntity<>(redeemedRewards, HttpStatus.OK);
    }

    public ResponseEntity<Object> viewRewardHistories(Long userId) throws UserNotFoundException {
        User user = userRepository.findUserById(userId);
        if (user == null)
        {
            throw new UserNotFoundException("User not found!");
        }
        util.clearUserLists(user);
        //List<Reward> redeemedRewards = rewardRepository.findRewardsByUser(user);
        List<RewardsHistory> rewardsHistories = rewardsHistoryRepository.findRewardsHistoriesByUser(user);
        return new ResponseEntity<>(rewardsHistories, HttpStatus.OK);
    }
}
