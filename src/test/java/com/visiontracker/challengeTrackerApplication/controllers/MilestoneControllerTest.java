package com.visiontracker.challengeTrackerApplication.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.visiontracker.challengeTrackerApplication.models.datamodels.CreateMilestoneReq;
import com.visiontracker.challengeTrackerApplication.models.datamodels.CreateProgramReq;
import com.visiontracker.challengeTrackerApplication.models.db.Milestone;
import com.visiontracker.challengeTrackerApplication.models.db.Program;
import com.visiontracker.challengeTrackerApplication.repositories.MilestoneRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

public class MilestoneControllerTest {
    @MockBean
    private MilestoneRepository milestoneRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    //creation of new milestone: success
    @Test
    public void testMilestoneController01() throws Exception {
            String stringDate = "12-05-2021";
            Milestone newMilestone = new Milestone("Sample Title", "Sample Description", "Individual", new Date(), null,
                    new BigDecimal(1000), new BigDecimal(5000), "Health", "No. of steps / day", 20);

            CreateMilestoneReq newMilestoneReq = new CreateMilestoneReq(newMilestone, 1l, stringDate);
            String requestContent = objectMapper.writeValueAsString(newMilestoneReq);
            System.out.println(requestContent);
            mockMvc.perform(MockMvcRequestBuilders.post("/Milestone/createMilestone").contentType(APPLICATION_JSON).content(requestContent))
                    .andExpect(MockMvcResultMatchers.status().isOk());
            Mockito.verify(milestoneRepository, Mockito.atMostOnce()).save(newMilestone);

    }

    //creation of new milestone: fail due to duplicate entry
    @Test
    public void testMilestoneController02() throws Exception {
        String stringDate = "12-05-2021";
        Milestone newMilestone = new Milestone("Sample Title", "Sample Description", "Individual", new Date(), null,
                new BigDecimal(1000), new BigDecimal(5000), "Health", "No. of steps / day", 20);

        CreateMilestoneReq newMilestoneReq = new CreateMilestoneReq(newMilestone, 1l, stringDate);
        String requestContent = objectMapper.writeValueAsString(newMilestoneReq);
        System.out.println(requestContent);
        mockMvc.perform(MockMvcRequestBuilders.post("/Milestone/createMilestone").contentType(APPLICATION_JSON).content(requestContent))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        //Mockito.verify(milestoneRepository, Mockito.atMostOnce()).save(newMilestone);
    }

    //creation of new milestone: failure due to null program ID
    @Test
    public void testMilestoneController03() throws Exception {
        String stringDate = "12-05-2021";
        Milestone newMilestone = new Milestone("Sample Title", "Sample Description", "Individual", new Date(), null,
                new BigDecimal(1000), new BigDecimal(5000), "Health", "No. of steps / day", 20);

        CreateMilestoneReq newMilestoneReq = new CreateMilestoneReq(newMilestone, null, stringDate);
        String requestContent = objectMapper.writeValueAsString(newMilestoneReq);
        System.out.println(requestContent);
        mockMvc.perform(MockMvcRequestBuilders.post("/Program/createProgram").contentType(APPLICATION_JSON).content(requestContent))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    //retrieval of program milestones
    @Test
    public void testMilestoneController04() throws Exception {
        Long programId = Long.valueOf(1);
        String requestContent = objectMapper.writeValueAsString(programId);
        System.out.println(requestContent);
        mockMvc.perform(MockMvcRequestBuilders.get("/Milestone/getProgramMilestones").contentType(APPLICATION_JSON).content(requestContent))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
