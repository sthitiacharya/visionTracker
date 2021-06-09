package com.visiontracker.challengeTrackerApplication.controllers;

import com.visiontracker.challengeTrackerApplication.models.db.RewardsHistory;
import com.visiontracker.challengeTrackerApplication.services.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        try
        {
            return rewardService.viewAllRewards();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/rewards/{rewardId}")
    public ResponseEntity<Object> viewRewardDetails(@PathVariable(name = "rewardId") Long rewardId)
    {
        try
        {
            return rewardService.viewRewardDetails(rewardId);
        }
        catch (RewardNotFoundException ex)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        catch(Exception ex)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PostMapping("/redeemReward")
    public ResponseEntity<Object> redeemReward(RewardsHistory rewardsHistory)
    {
        try
        {
            return rewardService.redeemReward(rewardsHistory);
        }
        catch(Exception ex)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/viewRedeemedRewards")
    public ResponseEntity<Object> viewRedeemedRewards(@RequestParam(name = "userId") Long userId)
    {
        try
        {
            return rewardService.viewRedeemedRewards(userId);
        }
        catch (UserNotFoundException ex)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
}
