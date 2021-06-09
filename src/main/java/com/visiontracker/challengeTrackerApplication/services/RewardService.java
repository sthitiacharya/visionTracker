package com.visiontracker.challengeTrackerApplication.services;

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
import util.exception.RewardNotFoundException;
import util.exception.UserNotFoundException;

import javax.xml.ws.Response;
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

    public ResponseEntity<Object> viewAllRewards()
    {
        List<Reward> rewards = rewardRepository.findAllRewards();
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

    public ResponseEntity<Object> redeemReward(RewardsHistory rewardHistory)
    {
        rewardHistory.setDateOfRedemption(new Date());
        rewardHistory.setRedeemPointValue(rewardHistory.getReward().getRedeemablePoints());

        RewardsHistory newRewardHistory = rewardsHistoryRepository.save(rewardHistory);

        newRewardHistory.getUser().setRewardPoints(newRewardHistory.getUser().getRewardPoints() - newRewardHistory.getRedeemPointValue());
        userRepository.save(newRewardHistory.getUser());
        clearLists(newRewardHistory.getUser());

        return new ResponseEntity<>(newRewardHistory.getRewardsHistoryId(), HttpStatus.OK);
    }

    public ResponseEntity<Object> viewRedeemedRewards(Long userId) throws UserNotFoundException {
        User user = userRepository.findUserById(userId);
        if (user == null)
        {
            throw new UserNotFoundException("User not found!");
        }
        clearLists(user);
        List<Reward> redeemedRewards = rewardRepository.findRewardsByUser(user);

        return new ResponseEntity<>(redeemedRewards, HttpStatus.OK);
    }

    private void clearLists(User user) {
        if (!user.getProgramsManaging().isEmpty())
        {
            user.getProgramsManaging().clear();
        }
        if (!user.getMilestoneList().isEmpty())
        {
            user.getMilestoneList().clear();
        }
        if (!user.getEnrolledPrograms().isEmpty())
        {
            user.getEnrolledPrograms().clear();
        }
        if (!user.getMilestonesCreated().isEmpty())
        {
            user.getMilestonesCreated().clear();
        }
    }
}
