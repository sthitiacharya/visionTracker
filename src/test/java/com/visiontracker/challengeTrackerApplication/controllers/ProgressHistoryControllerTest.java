package com.visiontracker.challengeTrackerApplication.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.visiontracker.challengeTrackerApplication.models.datamodels.CreateMilestoneReq;
import com.visiontracker.challengeTrackerApplication.models.datamodels.CreateProgHistoryReq;
import com.visiontracker.challengeTrackerApplication.models.db.Milestone;
import com.visiontracker.challengeTrackerApplication.models.db.Program;
import com.visiontracker.challengeTrackerApplication.models.db.ProgressHistory;
import com.visiontracker.challengeTrackerApplication.models.db.User;
import com.visiontracker.challengeTrackerApplication.repositories.MilestoneRepository;
import com.visiontracker.challengeTrackerApplication.repositories.ProgramRepository;
import com.visiontracker.challengeTrackerApplication.repositories.ProgressHistoryRepository;
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

import java.math.BigDecimal;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest
@AutoConfigureMockMvc
public class ProgressHistoryControllerTest {
    @MockBean
    private ProgressHistoryRepository progressHistoryRepository;

    @MockBean
    private MilestoneRepository milestoneRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ProgramRepository programRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    //creation of new progress history: success
    @Test
    public void testProgressHistoryController01() throws Exception {
        ProgressHistory newProgressHistory = new ProgressHistory(new Date(), new BigDecimal(2000));

        Program newProgram = new Program("Sample Title", "Sample Description", null, null);
        newProgram.setCurrentProgressRate(0.00);
        User user = new User("mail@mail.com", "username", "password", "Mailing Address Avenue");
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);
        userRepository.save(user);
        newProgram.setProgramManager(user);
        newProgram.setProgramId(1L);

        Milestone newMilestone = new Milestone("Sample Title", "Sample Description", "Individual", new Date(), null,
                new BigDecimal(1000), new BigDecimal(5000), "Health", "No. of steps / day", 20);
        Long milestoneId = Long.valueOf(1);
        newMilestone.setProgramId(newProgram);
        newMilestone.setMilestoneId(milestoneId);

        Mockito.when(progressHistoryRepository.save(any(ProgressHistory.class))).thenReturn(newProgressHistory);
        Mockito.when(milestoneRepository.findMilestoneByMilestoneId(milestoneId)).thenReturn(newMilestone);
        Mockito.when(programRepository.save(any(Program.class))).thenReturn(newProgram);

        CreateProgHistoryReq newProgHistReq = new CreateProgHistoryReq(newProgressHistory, milestoneId);
        String requestContent = objectMapper.writeValueAsString(newProgHistReq);
        System.out.println(requestContent);
        mockMvc.perform(MockMvcRequestBuilders.post("/ProgressHistory/logProgress").contentType(APPLICATION_JSON).content(requestContent))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(progressHistoryRepository, Mockito.atMostOnce()).save(newProgressHistory);
    }

    //creation of new progress history: failure due to invalid milestone
    @Test
    public void testProgressHistoryController02() throws Exception {
        ProgressHistory newProgressHistory = new ProgressHistory(new Date(), new BigDecimal(2000));

        Program newProgram = new Program("Sample Title", "Sample Description", null, null);
        newProgram.setCurrentProgressRate(0.00);
        User user = new User("mail@mail.com", "username", "password", "Mailing Address Avenue");
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);
        userRepository.save(user);
        newProgram.setProgramManager(user);
        newProgram.setProgramId(1L);

        Milestone newMilestone = new Milestone("Sample Title", "Sample Description", "Individual", new Date(), null,
                new BigDecimal(1000), new BigDecimal(5000), "Health", "No. of steps / day", 20);
        Long milestoneId = Long.valueOf(1);
        newMilestone.setProgramId(newProgram);
        newMilestone.setMilestoneId(milestoneId);

        Mockito.when(progressHistoryRepository.save(any(ProgressHistory.class))).thenReturn(newProgressHistory);
        Mockito.when(milestoneRepository.findMilestoneByMilestoneId(milestoneId)).thenReturn(newMilestone);
        Mockito.when(programRepository.save(any(Program.class))).thenReturn(newProgram);

        CreateProgHistoryReq newProgHistReq = new CreateProgHistoryReq(newProgressHistory, null);
        String requestContent = objectMapper.writeValueAsString(newProgHistReq);
        System.out.println(requestContent);
        mockMvc.perform(MockMvcRequestBuilders.post("/ProgressHistory/logProgress").contentType(APPLICATION_JSON).content(requestContent))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Milestone not found"));
    }
}
