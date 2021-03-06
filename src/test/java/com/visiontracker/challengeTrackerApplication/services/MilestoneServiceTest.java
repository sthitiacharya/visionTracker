package com.visiontracker.challengeTrackerApplication.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.visiontracker.challengeTrackerApplication.models.datamodels.CreateMilestoneReq;
import com.visiontracker.challengeTrackerApplication.models.datamodels.UpdateMilestoneReq;
import com.visiontracker.challengeTrackerApplication.models.db.Milestone;
import com.visiontracker.challengeTrackerApplication.models.db.Program;
import com.visiontracker.challengeTrackerApplication.models.db.User;
import com.visiontracker.challengeTrackerApplication.repositories.MilestoneRepository;
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
public class MilestoneServiceTest {
    @MockBean
    private MilestoneRepository milestoneRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    //creation of new milestone: success
    @Test
    public void createMilestoneSuccess() throws Exception {
        String stringDate = "12-05-2021";
        Milestone newMilestone = new Milestone("Sample Title", "Sample Description", "Program", new Date(), null,
                new BigDecimal(1000), new BigDecimal(5000), "Health", "No. of steps / day", 20);

        User u = userRepository.findUserByUserId(1L);
        newMilestone.setMilestoneCreatedBy(u);
        Long programId = Long.valueOf(1);

        Mockito.when(milestoneRepository.save(any(Milestone.class))).thenReturn(newMilestone);
        CreateMilestoneReq newMilestoneReq = new CreateMilestoneReq(newMilestone, programId, stringDate, null);
        String requestContent = objectMapper.writeValueAsString(newMilestoneReq);
        System.out.println(requestContent);
        mockMvc.perform(MockMvcRequestBuilders.post("/Milestone/createMilestone").contentType(APPLICATION_JSON).content(requestContent))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(milestoneRepository, Mockito.atMostOnce()).save(newMilestone);
    }

    //creation of new milestone: fail due to duplicate entry
    @Test
    public void createMilestoneFail01() throws Exception {
        String stringDate = "12-05-2021";
        Milestone newMilestone = new Milestone("Sample Title", "Sample Description", "Program", new Date(), null,
                new BigDecimal(1000), new BigDecimal(5000), "Health", "No. of steps / day", 20);
        User u = userRepository.findUserByUserId(Long.valueOf(1));
        newMilestone.setMilestoneCreatedBy(u);

        Mockito.when(milestoneRepository.save(any(Milestone.class))).thenReturn(newMilestone);
        Mockito.when(milestoneRepository.findMilestoneByTitle("Sample Title")).thenReturn(newMilestone);

        CreateMilestoneReq newMilestoneReq = new CreateMilestoneReq(newMilestone, 1l, stringDate, null);
        String requestContent = objectMapper.writeValueAsString(newMilestoneReq);
        System.out.println(requestContent);
        mockMvc.perform(MockMvcRequestBuilders.post("/Milestone/createMilestone").contentType(APPLICATION_JSON).content(requestContent));
        mockMvc.perform(MockMvcRequestBuilders.post("/Milestone/createMilestone").contentType(APPLICATION_JSON).content(requestContent))
                .andExpect(MockMvcResultMatchers.content().string("Duplicate Milestone Title"));
    }

    //creation of new milestone: failure due to null program ID
    @Test
    public void createMilestoneFail02() throws Exception {
        String stringDate = "12-05-2021";
        Milestone newMilestone = new Milestone("Sample Title", "Sample Description", "Program", new Date(), null,
                new BigDecimal(1000), new BigDecimal(5000), "Health", "No. of steps / day", 20);

        User u = userRepository.findUserByUserId(Long.valueOf(1));
        newMilestone.setMilestoneCreatedBy(u);

        CreateMilestoneReq newMilestoneReq = new CreateMilestoneReq(newMilestone, null, stringDate, null);
        String requestContent = objectMapper.writeValueAsString(newMilestoneReq);
        System.out.println(requestContent);
        mockMvc.perform(MockMvcRequestBuilders.post("/Milestone/createMilestone").contentType(APPLICATION_JSON).content(requestContent))
                .andExpect(MockMvcResultMatchers.content().string("Milestones need to be associated with a program"));
    }

    //creation of new milestone: failure due to invalid assigned user
    @Test
    public void createMilestoneFail03() throws Exception {
        String stringDate = "12-05-2021";
        Milestone newMilestone = new Milestone("Sample Title", "Sample Description", "Individual", new Date(), null,
                new BigDecimal(1000), new BigDecimal(5000), "Health", "No. of steps / day", 20);

        User u = userRepository.findUserByUserId(1L);
        newMilestone.setMilestoneCreatedBy(u);
        Long programId = 1L;

        Mockito.when(milestoneRepository.save(any(Milestone.class))).thenReturn(newMilestone);
        CreateMilestoneReq newMilestoneReq = new CreateMilestoneReq(newMilestone, programId, stringDate, null);
        String requestContent = objectMapper.writeValueAsString(newMilestoneReq);
        System.out.println(requestContent);
        mockMvc.perform(MockMvcRequestBuilders.post("/Milestone/createMilestone").contentType(APPLICATION_JSON).content(requestContent))
                .andExpect(MockMvcResultMatchers.content().string("There must be an assigned user for milestone of individual type"));
    }

    //retrieval of program milestones
    @Test
    public void retrieveProgramMilestoneSuccess() throws Exception {
        Long programId = Long.valueOf(1);
        String requestContent = objectMapper.writeValueAsString(programId);
        System.out.println(requestContent);
        mockMvc.perform(MockMvcRequestBuilders.get("/Milestone/getProgramMilestones").queryParam("programId", requestContent))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    //editing milestone: success
    @Test
    public void editMilestoneSuccess() throws Exception {
        String stringDate = "12-05-2021";
        Milestone newMilestone = new Milestone("Sample Title", "Sample Description", "Program", new Date(), null,
                new BigDecimal(1000), new BigDecimal(5000), "Health", "No. of steps / day", 20);

        User u = userRepository.findUserByUserId(1L);
        newMilestone.setMilestoneCreatedBy(u);
        Long programId = 1L;

        newMilestone.setMilestoneId(1L);
        newMilestone.setTitle("Updated Title");
        newMilestone.setDescription("Updated Description");
        Mockito.when(milestoneRepository.save(any(Milestone.class))).thenReturn(newMilestone);
        Mockito.when(milestoneRepository.findMilestoneByMilestoneId(1L)).thenReturn(newMilestone);
        UpdateMilestoneReq editMilestoneReq = new UpdateMilestoneReq(newMilestone, programId, stringDate, null);
        String requestContent = objectMapper.writeValueAsString(editMilestoneReq);
        System.out.println(requestContent);
        mockMvc.perform(MockMvcRequestBuilders.put("/Milestone/editMilestone/{milestoneId}", 1L).contentType(APPLICATION_JSON).content(requestContent))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(milestoneRepository, Mockito.atMostOnce()).save(newMilestone);
    }

    //editing milestone: failure due to invalid program association
    @Test
    public void editMilestoneFailure01() throws Exception {
        String stringDate = "12-05-2021";
        Milestone newMilestone = new Milestone("Sample Title", "Sample Description", "Individual", new Date(), null,
                new BigDecimal(1000), new BigDecimal(5000), "Health", "No. of steps / day", 20);

        User u = userRepository.findUserByUserId(1L);
        newMilestone.setMilestoneCreatedBy(u);
        Long programId = null;

        newMilestone.setMilestoneId(1L);
        newMilestone.setTitle("Updated Title");
        newMilestone.setDescription("Updated Description");
        Mockito.when(milestoneRepository.save(any(Milestone.class))).thenReturn(newMilestone);
        Mockito.when(milestoneRepository.findMilestoneByMilestoneId(1L)).thenReturn(newMilestone);
        UpdateMilestoneReq editMilestoneReq = new UpdateMilestoneReq(newMilestone, programId, stringDate, null);
        String requestContent = objectMapper.writeValueAsString(editMilestoneReq);
        System.out.println(requestContent);
        mockMvc.perform(MockMvcRequestBuilders.put("/Milestone/editMilestone/{milestoneId}", 1L).contentType(APPLICATION_JSON).content(requestContent))
                .andExpect(MockMvcResultMatchers.content().string("Milestones need to be associated with a program"));
    }

    //editing milestone: failure due to invalid assigned user
    @Test
    public void editMilestoneFailure02() throws Exception {
        String stringDate = "12-05-2021";
        Milestone newMilestone = new Milestone("Sample Title", "Sample Description", "Individual", new Date(), null,
                new BigDecimal(1000), new BigDecimal(5000), "Health", "No. of steps / day", 20);

        User u = userRepository.findUserByUserId(1L);
        newMilestone.setMilestoneCreatedBy(u);
        Long programId = 1L;

        newMilestone.setMilestoneId(1L);
        newMilestone.setTitle("Updated Title");
        newMilestone.setDescription("Updated Description");
        Mockito.when(milestoneRepository.save(any(Milestone.class))).thenReturn(newMilestone);
        Mockito.when(milestoneRepository.findMilestoneByMilestoneId(1L)).thenReturn(newMilestone);
        UpdateMilestoneReq editMilestoneReq = new UpdateMilestoneReq(newMilestone, programId, stringDate, null);
        String requestContent = objectMapper.writeValueAsString(editMilestoneReq);
        System.out.println(requestContent);
        mockMvc.perform(MockMvcRequestBuilders.put("/Milestone/editMilestone/{milestoneId}", 1L).contentType(APPLICATION_JSON).content(requestContent))
                .andExpect(MockMvcResultMatchers.content().string("There must be an assigned user for milestone of individual type"));
    }

    //deleting milestone: success
    @Test
    public void deleteMilestoneSuccess() throws Exception {
        String stringDate = "12-05-2021";
        Milestone newMilestone = new Milestone("Sample Title", "Sample Description", "Individual", new Date(), null,
                new BigDecimal(1000), new BigDecimal(5000), "Health", "No. of steps / day", 20);

        Program newProgram = new Program("Sample Title", "Sample Description", null, null);
        Long programId = 1L;
        newProgram.setProgramId(programId);
        newProgram.setCurrentProgressRate(0.00);
        User newUser = new User("email@email.com", "newUser", "password", "111 Address Avenue Singapore 123456");
        newUser.setUserId(1L);
        newProgram.setProgramManager(newUser);
        newMilestone.setMilestoneCreatedBy(newUser);
        newMilestone.setProgramId(newProgram);

        Mockito.when(milestoneRepository.findMilestoneByMilestoneId(1L)).thenReturn(newMilestone);
        Mockito.when(userRepository.findUserByUserId(1L)).thenReturn(newUser);
        mockMvc.perform(MockMvcRequestBuilders.delete("/Milestone/deleteMilestone/{milestoneId}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    //deleting milestone: failure due to invalid milestone ID
    @Test
    public void deleteMilestoneFailure() throws Exception {
        String stringDate = "12-05-2021";
        Milestone newMilestone = new Milestone("Sample Title", "Sample Description", "Individual", new Date(), null,
                new BigDecimal(1000), new BigDecimal(5000), "Health", "No. of steps / day", 20);

        User u = userRepository.findUserByUserId(1L);
        newMilestone.setMilestoneCreatedBy(u);
        Long programId = 1L;
        Mockito.when(milestoneRepository.findMilestoneByMilestoneId(1L)).thenReturn(newMilestone);
        mockMvc.perform(MockMvcRequestBuilders.delete("/Milestone/deleteMilestone/{milestoneId}", 2L))
                .andExpect(MockMvcResultMatchers.content().string("Milestone not found"));
    }

}
