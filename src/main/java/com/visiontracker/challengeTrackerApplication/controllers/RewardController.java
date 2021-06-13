package com.visiontracker.challengeTrackerApplication.controllers;

import com.visiontracker.challengeTrackerApplication.services.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import util.exception.RedeemRewardException;
import util.exception.RewardNotFoundException;
import util.exception.UserNotFoundException;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path="/Reward")
public class RewardController {
    @Autowired
    private RewardService rewardService;

    @GetMapping("/rewards")
    public ResponseEntity<Object> viewAllRewards()
    {
        return rewardService.viewAllRewards();
    }

    @GetMapping("/rewards/{rewardId}")
    public ResponseEntity<Object> viewRewardDetails(@PathVariable(name = "rewardId") Long rewardId) throws RewardNotFoundException {
        return rewardService.viewRewardDetails(rewardId);
    }

    @PostMapping("/redeemReward")
    public ResponseEntity<Object> redeemReward(@RequestParam(name = "rewardId")Long rewardId,
                                               @RequestParam(name = "userId")Long userId) throws UserNotFoundException, RewardNotFoundException, RedeemRewardException {
        return rewardService.redeemReward(rewardId, userId);
    }

    @GetMapping("/viewRedeemedRewards")
    public ResponseEntity<Object> viewRedeemedRewards(@RequestParam(name = "userId") Long userId) throws UserNotFoundException {
        return rewardService.viewRedeemedRewards(userId);
    }

    @GetMapping("/viewRewardsHistories")
    public ResponseEntity<Object> viewRewardsHistories(@RequestParam(name = "userId") Long userId) throws UserNotFoundException {
        return rewardService.viewRewardHistories(userId);
    }
}
