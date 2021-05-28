package com.visiontracker.challengeTrackerApplication.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.visiontracker.challengeTrackerApplication.models.datamodels.LoginReq;
import com.visiontracker.challengeTrackerApplication.models.db.User;
import com.visiontracker.challengeTrackerApplication.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    //creation of new user: success
    @Test
    public void testUserController01() throws Exception
    {
        User newUser = new User("email@email.com", "newUser", "password", "111 Address Avenue Singapore 123456");
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String requestContent = objectMapper.writeValueAsString(newUser);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/User/register").contentType(APPLICATION_JSON).content(requestContent))
        .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(userRepository, Mockito.atMostOnce()).save(newUser);
    }

    //user login: success
    @Test
    public void testUserController02() throws Exception
    {
        User u = new User();
        u.setUsername("username");
        u.setPassword("password");
        Mockito.when(userRepository.findUserByUsername("username")).thenReturn(u);
        LoginReq userToLogin = new LoginReq("username", "password");
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String requestContent = objectMapper.writeValueAsString(userToLogin);
        mockMvc.perform(MockMvcRequestBuilders.post("/User/login").contentType(APPLICATION_JSON).content(requestContent))
                .andExpect(MockMvcResultMatchers.status().isOk());
        //Mockito.verify(userRepository, Mockito.atMostOnce()).save(newUser);
    }

    //user login: failure due to invalid username
    @Test
    public void testUserController03() throws Exception
    {
        User u = new User();
        u.setUsername("username");
        u.setPassword("password");
        Mockito.when(userRepository.findUserByUsername("username")).thenReturn(u);
        LoginReq userToLogin = new LoginReq("user", "password");
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String requestContent = objectMapper.writeValueAsString(userToLogin);
        //System.out.println(requestContent);
        mockMvc.perform(MockMvcRequestBuilders.post("/User/login").contentType(APPLICATION_JSON).content(requestContent))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
        .andExpect(MockMvcResultMatchers.content().string("Username not found"));
    }

    //user login: failure due to invalid password
    @Test
    public void testUserController04() throws Exception
    {
        User u = new User();
        u.setUsername("username");
        u.setPassword("password");
        Mockito.when(userRepository.findUserByUsername("username")).thenReturn(u);
        LoginReq userToLogin = new LoginReq("username", "qwerty");
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String requestContent = objectMapper.writeValueAsString(userToLogin);
        //System.out.println(requestContent);
        mockMvc.perform(MockMvcRequestBuilders.post("/User/login").contentType(APPLICATION_JSON).content(requestContent))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().string("Invalid Password"));
    }
}
