package com.visiontracker.challengeTrackerApplication.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.visiontracker.challengeTrackerApplication.models.db.Reward;
import com.visiontracker.challengeTrackerApplication.models.db.RewardsHistory;
import com.visiontracker.challengeTrackerApplication.models.db.User;
import com.visiontracker.challengeTrackerApplication.repositories.RewardRepository;
import com.visiontracker.challengeTrackerApplication.repositories.RewardsHistoryRepository;
import com.visiontracker.challengeTrackerApplication.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import util.exception.RedeemRewardException;
import util.exception.RewardNotFoundException;
import util.exception.UserNotFoundException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class RewardServiceTest {
    @MockBean
    private RewardRepository rewardRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RewardsHistoryRepository rewardsHistoryRepository;

    @InjectMocks
    private RewardService rewardService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void viewAllRewardsSuccess() throws Exception
    {
        Reward reward1 = new Reward("RV0001", " $10 Grocery Store Vouchers",
                "The vouchers can be redeemed at any Grocery Store outlet in Singapore" +
                        ". Valid for only one month from date of redemption", "Voucher",
                20);
        Reward reward2 = new Reward("RV0002", " $20 Grocery Store Vouchers",
                "The vouchers can be redeemed at any Grocery Store outlet in Singapore" +
                        ". Valid for only one month from date of redemption", "Voucher",
                40);
        List<Reward> rewards = new ArrayList<>();
        rewards.add(reward1);
        rewards.add(reward2);
        Mockito.when(rewardRepository.findAll()).thenReturn(rewards);
        Object expected = rewardService.viewAllRewards().getBody();
        System.out.println("Expected Body" + expected.toString());
        Assertions.assertEquals(rewards, expected);
    }

    @Test
    public void retrieveIndividualRewardSuccess() throws RewardNotFoundException {
        Reward reward = new Reward("RV0002", " $20 Grocery Store Vouchers",
                "The vouchers can be redeemed at any Grocery Store outlet in Singapore" +
                        ". Valid for only one month from date of redemption", "Voucher",
                40);
        reward.setRewardId(1L);
        Mockito.when(rewardRepository.findRewardByRewardId(1L)).thenReturn(reward);
        Object expected = rewardService.viewRewardDetails(1L).getBody();
        System.out.println("Expected Body" + expected.toString());
        Assertions.assertEquals(reward, expected);
    }

    @Test
    public void retrieveIndividualRewardFail() {
        Reward reward = new Reward("RV0002", " $20 Grocery Store Vouchers",
                "The vouchers can be redeemed at any Grocery Store outlet in Singapore" +
                        ". Valid for only one month from date of redemption", "Voucher",
                40);
        reward.setRewardId(1L);
        Mockito.when(rewardRepository.findRewardByRewardId(1L)).thenReturn(reward);

        Assertions.assertThrows(RewardNotFoundException.class, () -> {
            rewardService.viewRewardDetails(2L);
        });
    }

    @Test
    public void redeemRewardSuccess() throws UserNotFoundException, RewardNotFoundException, RedeemRewardException, ParseException {
        Reward reward = new Reward("RV0002", " $20 Grocery Store Vouchers",
                "The vouchers can be redeemed at any Grocery Store outlet in Singapore" +
                        ". Valid for only one month from date of redemption", "Voucher",
                40);
        reward.setRewardId(1L);
        Mockito.when(rewardRepository.findRewardByRewardId(1L)).thenReturn(reward);

        User newUser = new User("email@email.com", "newUser", "password", "111 Address Avenue Singapore 123456");
        newUser.setUserId(1L);
        newUser.setRewardPoints(50);
        Mockito.when(userRepository.findUserByUserId(1L)).thenReturn(newUser);

        RewardsHistory rewardsHistory = new RewardsHistory();
        rewardsHistory.setReward(reward); rewardsHistory.setUser(newUser);
        Mockito.when(rewardsHistoryRepository.save(any(RewardsHistory.class))).thenReturn(rewardsHistory);

        HttpStatus expectedStatusCode = rewardService.redeemReward(1L, 1L).getStatusCode();
        Assertions.assertEquals(expectedStatusCode, HttpStatus.OK);
    }

    //expected RewardNotFoundException
    @Test
    public void redeemRewardFailure01() {
        Reward reward = new Reward("RV0002", " $20 Grocery Store Vouchers",
                "The vouchers can be redeemed at any Grocery Store outlet in Singapore" +
                        ". Valid for only one month from date of redemption", "Voucher",
                40);
        reward.setRewardId(1L);
        Mockito.when(rewardRepository.findRewardByRewardId(1L)).thenReturn(reward);

        User newUser = new User("email@email.com", "newUser", "password", "111 Address Avenue Singapore 123456");
        newUser.setUserId(1L);
        newUser.setRewardPoints(50);
        Mockito.when(userRepository.findUserByUserId(1L)).thenReturn(newUser);

        Assertions.assertThrows(RewardNotFoundException.class, () -> {
            rewardService.redeemReward(2L, 1L);
        });
    }

    //expected UserNotFoundException
    @Test
    public void redeemRewardFailure02() {
        Reward reward = new Reward("RV0002", " $20 Grocery Store Vouchers",
                "The vouchers can be redeemed at any Grocery Store outlet in Singapore" +
                        ". Valid for only one month from date of redemption", "Voucher",
                40);
        reward.setRewardId(1L);
        Mockito.when(rewardRepository.findRewardByRewardId(1L)).thenReturn(reward);

        User newUser = new User("email@email.com", "newUser", "password", "111 Address Avenue Singapore 123456");
        newUser.setUserId(1L);
        newUser.setRewardPoints(50);
        Mockito.when(userRepository.findUserByUserId(1L)).thenReturn(newUser);

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            rewardService.redeemReward(1L, 2L);
        });
    }

    //expected RedeemRewardException
    @Test
    public void redeemRewardFailure03() {
        Reward reward = new Reward("RV0002", " $20 Grocery Store Vouchers",
                "The vouchers can be redeemed at any Grocery Store outlet in Singapore" +
                        ". Valid for only one month from date of redemption", "Voucher",
                40);
        reward.setRewardId(1L);
        Mockito.when(rewardRepository.findRewardByRewardId(1L)).thenReturn(reward);

        User newUser = new User("email@email.com", "newUser", "password", "111 Address Avenue Singapore 123456");
        newUser.setUserId(1L);
        newUser.setRewardPoints(20);
        Mockito.when(userRepository.findUserByUserId(1L)).thenReturn(newUser);

        Assertions.assertThrows(RedeemRewardException.class, () -> {
            rewardService.redeemReward(1L, 1L);
        });
    }

    @Test
    public void viewRedeemedRewardsSuccess() throws UserNotFoundException {
        Reward reward1 = new Reward("RV0001", " $10 Grocery Store Vouchers",
                "The vouchers can be redeemed at any Grocery Store outlet in Singapore" +
                        ". Valid for only one month from date of redemption", "Voucher",
                20);
        Reward reward2 = new Reward("RV0002", " $20 Grocery Store Vouchers",
                "The vouchers can be redeemed at any Grocery Store outlet in Singapore" +
                        ". Valid for only one month from date of redemption", "Voucher",
                40);
        List<Reward> rewards = new ArrayList<>();
        rewards.add(reward1);
        rewards.add(reward2);

        User newUser = new User("email@email.com", "newUser", "password", "111 Address Avenue Singapore 123456");
        newUser.setUserId(1L);
        newUser.setRewardPoints(50);
        Mockito.when(userRepository.findUserByUserId(1L)).thenReturn(newUser);

        Mockito.when(rewardRepository.findRewardsByUser(newUser)).thenReturn(rewards);

        Object expected = rewardService.viewRedeemedRewards(1L).getBody();
        System.out.println("Expected Body" + expected);
        Assertions.assertEquals(rewards, expected);
    }

    @Test
    public void viewRewardHistoriesSuccess() throws UserNotFoundException {
        Reward reward1 = new Reward("RV0001", " $10 Grocery Store Vouchers",
                "The vouchers can be redeemed at any Grocery Store outlet in Singapore" +
                        ". Valid for only one month from date of redemption", "Voucher",
                20);
        Reward reward2 = new Reward("RV0002", " $20 Grocery Store Vouchers",
                "The vouchers can be redeemed at any Grocery Store outlet in Singapore" +
                        ". Valid for only one month from date of redemption", "Voucher",
                40);

        User newUser = new User("email@email.com", "newUser", "password", "111 Address Avenue Singapore 123456");
        newUser.setUserId(1L);
        newUser.setRewardPoints(50);
        Mockito.when(userRepository.findUserByUserId(1L)).thenReturn(newUser);

        RewardsHistory rewardsHistory1 = new RewardsHistory();
        rewardsHistory1.setReward(reward1); rewardsHistory1.setUser(newUser);
        RewardsHistory rewardsHistory2 = new RewardsHistory();
        rewardsHistory2.setReward(reward2); rewardsHistory2.setUser(newUser);

        List<RewardsHistory> rewardsHistories = new ArrayList<>();
        rewardsHistories.add(rewardsHistory1); rewardsHistories.add(rewardsHistory2);

        Mockito.when(rewardsHistoryRepository.findRewardsHistoriesByUser(newUser)).thenReturn(rewardsHistories);

        Object expected = rewardService.viewRewardHistories(1L).getBody();
        System.out.println("Expected Body" + expected);
        Assertions.assertEquals(rewardsHistories, expected);
    }
}
