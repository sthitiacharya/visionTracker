package com.visiontracker.challengeTrackerApplication.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.visiontracker.challengeTrackerApplication.models.datamodels.CreateProgramReq;
import com.visiontracker.challengeTrackerApplication.models.db.Program;
import com.visiontracker.challengeTrackerApplication.repositories.ProgramRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

public class ProgramControllerTest {
    @MockBean
    private ProgramRepository programRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    //creation of new program: success
    @Test
    public void testProgramController01() throws Exception
    {
        String stringDate = "12-05-2021";
        String stringDate2 = "17-05-2021";
        Program newProgram = new Program("Sample Title", "Sample Description", null, null);
        List<Long> users = new ArrayList<>();
        users.add(1l);
        CreateProgramReq newProgramReq = new CreateProgramReq(newProgram, 1l, users, stringDate, stringDate2);
        String requestContent = objectMapper.writeValueAsString(newProgramReq);
        System.out.println(requestContent);
        mockMvc.perform(MockMvcRequestBuilders.post("/Program/createProgram").contentType(APPLICATION_JSON).content(requestContent))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(programRepository, Mockito.atMostOnce()).save(newProgram);
    }

    //creation of new program: fail due to duplicate entry of title
    @Test
    public void testProgramController02() throws Exception
    {
        String stringDate = "12-05-2021";
        String stringDate2 = "17-05-2021";
        Date date = new SimpleDateFormat("dd-MM-yyyy").parse(stringDate);
        Date date2 = new SimpleDateFormat("dd-MM-yyyy").parse(stringDate2);
        Program newProgram = new Program("Sample Title", "Sample Description", date, date2);
        List<Long> users = new ArrayList<>();
        users.add(1l);
        CreateProgramReq newProgramReq = new CreateProgramReq(newProgram, 1l, users, stringDate, stringDate2);
        String requestContent = objectMapper.writeValueAsString(newProgramReq);
        System.out.println(requestContent);
        mockMvc.perform(MockMvcRequestBuilders.post("/Program/createProgram").contentType(APPLICATION_JSON).content(requestContent))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        //Mockito.verify(programRepository, Mockito.atMostOnce()).save(newProgram);
    }

    //creation of new program: fail due to null program manager
    @Test
    public void testProgramController03() throws Exception
    {
        String stringDate = "12-05-2021";
        String stringDate2 = "17-05-2021";
        Date date = new SimpleDateFormat("dd-MM-yyyy").parse(stringDate);
        Date date2 = new SimpleDateFormat("dd-MM-yyyy").parse(stringDate2);
        Program newProgram = new Program("Sample Title", "Sample Description", date, date2);
        List<Long> users = new ArrayList<>();
        users.add(1l);
        CreateProgramReq newProgramReq = new CreateProgramReq(newProgram, null, users, stringDate, stringDate2);
        String requestContent = objectMapper.writeValueAsString(newProgramReq);
        System.out.println(requestContent);
        mockMvc.perform(MockMvcRequestBuilders.post("/Program/createProgram").contentType(APPLICATION_JSON).content(requestContent))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        //Mockito.verify(programRepository, Mockito.atMostOnce()).save(newProgram);
    }

    //retrieval of enrolled programs
    @Test
    public void testProgramController04() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.get("/Program/getEnrolledPrograms").contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        //Mockito.verify(programRepository, Mockito.atMostOnce()).findAll();
    }

    //retrieval of individual program
    @Test
    public void testProgramController05() throws Exception
    {
        Long programId = Long.valueOf(1);
        String requestContent = objectMapper.writeValueAsString(programId);
        mockMvc.perform(MockMvcRequestBuilders.get("/Program/getEnrolledPrograms").contentType(APPLICATION_JSON).content(requestContent))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(programRepository, Mockito.atMostOnce()).findProgramById(programId);
    }
}
