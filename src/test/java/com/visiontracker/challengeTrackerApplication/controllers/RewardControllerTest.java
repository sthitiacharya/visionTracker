package com.visiontracker.challengeTrackerApplication.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.visiontracker.challengeTrackerApplication.models.db.Reward;
import com.visiontracker.challengeTrackerApplication.models.db.RewardsHistory;
import com.visiontracker.challengeTrackerApplication.models.db.User;
import com.visiontracker.challengeTrackerApplication.repositories.RewardRepository;
import com.visiontracker.challengeTrackerApplication.repositories.RewardsHistoryRepository;
import com.visiontracker.challengeTrackerApplication.repositories.UserRepository;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@AutoConfigureMockMvc
public class RewardControllerTest {
    @MockBean
    private RewardRepository rewardRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RewardsHistoryRepository rewardsHistoryRepository;

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

        mockMvc.perform(MockMvcRequestBuilders.get("/Reward/rewards"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void retrieveIndividualRewardSuccess() throws Exception {
        Reward reward = new Reward("RV0002", " $20 Grocery Store Vouchers",
                "The vouchers can be redeemed at any Grocery Store outlet in Singapore" +
                        ". Valid for only one month from date of redemption", "Voucher",
                40);
        reward.setRewardId(1L);
        Mockito.when(rewardRepository.findRewardByRewardId(1L)).thenReturn(reward);

        mockMvc.perform(MockMvcRequestBuilders.get("/Reward/rewards/{rewardId}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void retrieveIndividualRewardFail() throws Exception {
        Reward reward = new Reward("RV0002", " $20 Grocery Store Vouchers",
                "The vouchers can be redeemed at any Grocery Store outlet in Singapore" +
                        ". Valid for only one month from date of redemption", "Voucher",
                40);
        reward.setRewardId(1L);
        Mockito.when(rewardRepository.findRewardByRewardId(1L)).thenReturn(reward);

        mockMvc.perform(MockMvcRequestBuilders.get("/Reward/rewards/{rewardId}", 2L))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Reward not found"));
    }

    @Test
    public void redeemRewardSuccess() throws Exception {
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

        String requestContent = objectMapper.writeValueAsString(1L);
        mockMvc.perform(MockMvcRequestBuilders.post("/Reward/redeemReward").param("rewardId", requestContent).param("userId", requestContent))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    //expected RewardNotFoundException
    @Test
    public void redeemRewardFailure01() throws Exception {
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

        String requestContent1 = objectMapper.writeValueAsString(1L);
        String requestContent2 = objectMapper.writeValueAsString(2L);

        mockMvc.perform(MockMvcRequestBuilders.post("/Reward/redeemReward").param("rewardId", requestContent2).param("userId", requestContent1))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Reward not found"));
    }

    //expected UserNotFoundException
    @Test
    public void redeemRewardFailure02() throws Exception {
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

        String requestContent = objectMapper.writeValueAsString(1L);
        String requestContent2 = objectMapper.writeValueAsString(2L);

        mockMvc.perform(MockMvcRequestBuilders.post("/Reward/redeemReward").param("rewardId", requestContent).param("userId", requestContent2))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("User not found"));
    }

    //expected RedeemRewardException
    @Test
    public void redeemRewardFailure03() throws Exception {
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

        String requestContent = objectMapper.writeValueAsString(1L);
        mockMvc.perform(MockMvcRequestBuilders.post("/Reward/redeemReward").param("rewardId", requestContent).param("userId", requestContent))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("You have insufficient reward points to redeem this reward"));
    }

    @Test
    public void viewRedeemedRewardsSuccess() throws Exception {
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

        String requestContent = objectMapper.writeValueAsString(1L);
        mockMvc.perform(MockMvcRequestBuilders.get("/Reward/viewRedeemedRewards").param("userId", requestContent))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void viewRewardHistoriesSuccess() throws Exception {
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

        String requestContent = objectMapper.writeValueAsString(1L);
        mockMvc.perform(MockMvcRequestBuilders.get("/Reward/viewRewardsHistories").param("userId", requestContent))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
