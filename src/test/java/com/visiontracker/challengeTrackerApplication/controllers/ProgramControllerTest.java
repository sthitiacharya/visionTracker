package com.visiontracker.challengeTrackerApplication.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.visiontracker.challengeTrackerApplication.models.datamodels.CreateProgramReq;
import com.visiontracker.challengeTrackerApplication.models.db.Program;
import com.visiontracker.challengeTrackerApplication.repositories.ProgramRepository;
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
    public void testProgramController02() throws Exception
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
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Duplicate Program Title"));
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
        users.add(1L);
        CreateProgramReq newProgramReq = new CreateProgramReq(newProgram, null, users, stringDate, stringDate2);
        String requestContent = objectMapper.writeValueAsString(newProgramReq);
        System.out.println(requestContent);
        mockMvc.perform(MockMvcRequestBuilders.post("/Program/createProgram").contentType(APPLICATION_JSON).content(requestContent))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Program must be assigned to a program manager"));
    }

    //retrieval of enrolled programs
    @Test
    public void testProgramController04() throws Exception
    {
        Long userId = 1L;
        String requestContent = objectMapper.writeValueAsString(userId);
        mockMvc.perform(MockMvcRequestBuilders.get("/Program/getEnrolledPrograms").queryParam("userId", requestContent))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    //retrieval of individual program
    @Test
    public void testProgramController05() throws Exception
    {
        Program p = new Program();
        p.setProgramId(1L);
        Mockito.when(programRepository.findProgramById(1L)).thenReturn(p);
        Long programId = 1L;
        String requestContent = objectMapper.writeValueAsString(programId);
        mockMvc.perform(MockMvcRequestBuilders.get("/Program/getEnrolledPrograms/{programId}", programId).param("programId", requestContent))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(programRepository, Mockito.atMostOnce()).findProgramById(programId);
    }
}
