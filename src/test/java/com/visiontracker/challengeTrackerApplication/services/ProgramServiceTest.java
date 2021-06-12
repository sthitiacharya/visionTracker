package com.visiontracker.challengeTrackerApplication.services;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.visiontracker.challengeTrackerApplication.models.datamodels.CreateProgramReq;
import com.visiontracker.challengeTrackerApplication.models.datamodels.UpdateProgramReq;
import com.visiontracker.challengeTrackerApplication.models.db.Program;
import com.visiontracker.challengeTrackerApplication.models.db.User;
import com.visiontracker.challengeTrackerApplication.repositories.ProgramRepository;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest
@AutoConfigureMockMvc
public class ProgramServiceTest {
    @MockBean
    private ProgramRepository programRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    //creation of new program: success
    @Test
    public void createProgramSuccess() throws Exception
    {
        String stringDate = "12-05-2021";
        String stringDate2 = "17-05-2021";
        Program newProgram = new Program("Sample Title", "Sample Description", null, null);
        User user = new User("mail@mail.com", "username", "password", "Mailing Address Avenue");
        Mockito.when(userRepository.findUserById(1L)).thenReturn(user);
        Mockito.when(programRepository.save(any(Program.class))).thenReturn(newProgram);
        List<Long> users = new ArrayList<>();
        users.add(1L);
        CreateProgramReq newProgramReq = new CreateProgramReq(newProgram, 1L, users, stringDate, stringDate2);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String requestContent = objectMapper.writeValueAsString(newProgramReq);
        //System.out.println(requestContent);
        mockMvc.perform(MockMvcRequestBuilders.post("/Program/createProgram").contentType(APPLICATION_JSON).content(requestContent))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(programRepository, Mockito.atMostOnce()).save(newProgram);
    }

    //creation of new program: fail due to duplicate entry of title
    @Test
    public void createProgramFailure01() throws Exception
    {
        String stringDate = "12-05-2021";
        String stringDate2 = "17-05-2021";
        Date date = new SimpleDateFormat("dd-MM-yyyy").parse(stringDate);
        Date date2 = new SimpleDateFormat("dd-MM-yyyy").parse(stringDate2);
        Program newProgram = new Program("Sample Title", "Sample Description", date, date2);
        List<Long> users = new ArrayList<>();
        users.add(1L);
        Mockito.when(programRepository.save(any(Program.class))).thenReturn(newProgram);
        Mockito.when(programRepository.findProgramByTitle("Sample Title")).thenReturn(newProgram);
        CreateProgramReq newProgramReq = new CreateProgramReq(newProgram, 1L, users, stringDate, stringDate2);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String requestContent = objectMapper.writeValueAsString(newProgramReq);
        System.out.println(requestContent);
        mockMvc.perform(MockMvcRequestBuilders.post("/Program/createProgram").contentType(APPLICATION_JSON).content(requestContent));
        mockMvc.perform(MockMvcRequestBuilders.post("/Program/createProgram").contentType(APPLICATION_JSON).content(requestContent))
                .andExpect(MockMvcResultMatchers.content().string("Duplicate Program Title"));
    }

    //creation of new program: fail due to null program manager
    @Test
    public void createProgramFailure02() throws Exception
    {
        String stringDate = "12-05-2021";
        String stringDate2 = "17-05-2021";
        Date date = new SimpleDateFormat("dd-MM-yyyy").parse(stringDate);
        Date date2 = new SimpleDateFormat("dd-MM-yyyy").parse(stringDate2);
        Program newProgram = new Program("Sample Title", "Sample Description", date, date2);
        List<Long> users = new ArrayList<>();
        users.add(1L);
        CreateProgramReq newProgramReq = new CreateProgramReq(newProgram, null, users, stringDate, stringDate2);
        String requestContent = objectMapper.writeValueAsString(newProgramReq);
        System.out.println(requestContent);
        mockMvc.perform(MockMvcRequestBuilders.post("/Program/createProgram").contentType(APPLICATION_JSON).content(requestContent))
                .andExpect(MockMvcResultMatchers.content().string("Program must be assigned to a program manager"));
    }

    //retrieval of enrolled programs
    @Test
    public void retrieveEnrolledProgramsSuccess() throws Exception
    {
        User user = new User("mail@mail.com", "username", "password", "Mailing Address Avenue");
        Mockito.when(userRepository.findUserById(1L)).thenReturn(user);
        Long userId = 1L;
        String requestContent = objectMapper.writeValueAsString(userId);
        mockMvc.perform(MockMvcRequestBuilders.get("/Program/getEnrolledPrograms").queryParam("userId", requestContent))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    //retrieval of individual program
    @Test
    public void retrieveIndividualProgramSuccess() throws Exception
    {
        Program p = new Program();
        p.setProgramId(1L);
        Mockito.when(programRepository.findProgramByProgramId(1L)).thenReturn(p);
        Long programId = 1L;
        String requestContent = objectMapper.writeValueAsString(programId);
        mockMvc.perform(MockMvcRequestBuilders.get("/Program/getEnrolledPrograms/{programId}", programId).param("programId", requestContent))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(programRepository, Mockito.atMostOnce()).findProgramByProgramId(programId);
    }

    //edit program: success
    @Test
    public void editProgramSuccess() throws Exception
    {
        String stringDate = "12-05-2021";
        String stringDate2 = "17-05-2021";
        Program newProgram = new Program("Sample Title", "Sample Description", null, null);
        User user = new User("mail@mail.com", "username", "password", "Mailing Address Avenue");
        newProgram.setProgramManager(user);
        Mockito.when(userRepository.findUserById(1L)).thenReturn(user);
        Mockito.when(programRepository.save(any(Program.class))).thenReturn(newProgram);
        List<Long> users = new ArrayList<>();
        users.add(1L);
        //CreateProgramReq newProgramReq = new CreateProgramReq(newProgram, 1L, users, stringDate, stringDate2);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //String requestContent1 = objectMapper.writeValueAsString(newProgramReq);
        //System.out.println(requestContent);
        //MvcResult programId = mockMvc.perform(MockMvcRequestBuilders.post("/Program/createProgram").contentType(APPLICATION_JSON).content(requestContent1)).andReturn();
        //Mockito.verify(programRepository, Mockito.atMostOnce()).save(newProgram);

        newProgram.setProgramId(1L);
        newProgram.setTitle("Updated Title");
        newProgram.setDescription("Updated description");
        Mockito.when(programRepository.findProgramByProgramId(1L)).thenReturn(newProgram);
        UpdateProgramReq editProgramReq = new UpdateProgramReq(newProgram, 1L, users, stringDate, stringDate2, 1L);
        String requestContent2 = objectMapper.writeValueAsString(editProgramReq);
        mockMvc.perform(MockMvcRequestBuilders.put("/Program/editProgram").contentType(APPLICATION_JSON).content(requestContent2))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    //edit program: failure as user is not program manager
    @Test
    public void editProgramFailure() throws Exception
    {
        String stringDate = "12-05-2021";
        String stringDate2 = "17-05-2021";
        Program newProgram = new Program("Sample Title", "Sample Description", null, null);
        User user1 = new User("mail@mail.com", "username", "password", "Mailing Address Avenue");
        User user2 = new User("user2@mail.com", "username2", "password", "Mailing Address Avenue");
        user2.setUserId(2L);
        newProgram.setProgramManager(user2);
        Mockito.when(userRepository.findUserById(1L)).thenReturn(user1);
        Mockito.when(programRepository.save(any(Program.class))).thenReturn(newProgram);
        List<Long> users = new ArrayList<>();
        users.add(1L);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        newProgram.setProgramId(1L);
        newProgram.setTitle("Updated Title");
        newProgram.setDescription("Updated description");
        Mockito.when(programRepository.findProgramByProgramId(1L)).thenReturn(newProgram);
        UpdateProgramReq editProgramReq = new UpdateProgramReq(newProgram, 1L, users, stringDate, stringDate2, 1L);
        String requestContent2 = objectMapper.writeValueAsString(editProgramReq);
        mockMvc.perform(MockMvcRequestBuilders.put("/Program/editProgram").contentType(APPLICATION_JSON).content(requestContent2))
                .andExpect(MockMvcResultMatchers.content().string("Program can only be updated by the program manager"));
    }

    //deletion of program: success
    @Test
    public void deleteProgramSuccess() throws Exception
    {
        String stringDate = "12-05-2021";
        String stringDate2 = "17-05-2021";
        Program newProgram = new Program("Sample Title", "Sample Description", null, null);
        User user = new User("mail@mail.com", "username", "password", "Mailing Address Avenue");
        newProgram.setProgramManager(user);
        Mockito.when(userRepository.findUserById(1L)).thenReturn(user);
        Mockito.when(programRepository.save(any(Program.class))).thenReturn(newProgram);
        List<Long> users = new ArrayList<>();
        users.add(1L);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        newProgram.setProgramId(1L);
        Mockito.when(programRepository.findProgramByProgramId(1L)).thenReturn(newProgram);
        //String requestContent2 = objectMapper.writeValueAsString(editProgramReq);
        mockMvc.perform(MockMvcRequestBuilders.delete("/Program/deleteProgram/{programId}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    //deletion of program: failure
    @Test
    public void deleteProgramFailure() throws Exception
    {
        String stringDate = "12-05-2021";
        String stringDate2 = "17-05-2021";
        Program newProgram = new Program("Sample Title", "Sample Description", null, null);
        User user = new User("mail@mail.com", "username", "password", "Mailing Address Avenue");
        newProgram.setProgramManager(user);
        Mockito.when(userRepository.findUserById(1L)).thenReturn(user);
        Mockito.when(programRepository.save(any(Program.class))).thenReturn(newProgram);
        List<Long> users = new ArrayList<>();
        users.add(1L);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        newProgram.setProgramId(1L);
        Mockito.when(programRepository.findProgramByProgramId(1L)).thenReturn(newProgram);
        //String requestContent2 = objectMapper.writeValueAsString(editProgramReq);
        mockMvc.perform(MockMvcRequestBuilders.delete("/Program/deleteProgram/{programId}", 2L))
                .andExpect(MockMvcResultMatchers.content().string("Program not found"));
    }
}
