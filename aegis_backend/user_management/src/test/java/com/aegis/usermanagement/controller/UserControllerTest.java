package com.aegis.usermanagement.controller;

import com.aegis.usermanagement.exception.InvalidUser;
import com.aegis.usermanagement.exception.UserNotFound;
import com.aegis.usermanagement.model.User;
import com.aegis.usermanagement.pojos.UserRequest;
import com.aegis.usermanagement.pojos.Users;
import com.aegis.usermanagement.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {UserController.class})
class UserControllerTest {

    @Autowired
    ObjectMapper mapper;


    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;


    @Test
    void getUser() throws Exception{
        when(userService.getUser(any())).thenReturn(new User());
        mockMvc.perform(get("/api/users/"+UUID.randomUUID())).andExpect(status().isOk());
    }

    @Test
    void getAllUsers() throws Exception{
        List<User> users= new ArrayList<>();
        User user= new User();
        user.setUserEmail("sreetej@gmail.in");
        user.setUserType(User.UserType.EMPLOYEE);
        user.setId(UUID.randomUUID());
        users.add(user);
        when(userService.getAllUsers()).thenReturn(users);
        mockMvc.perform(get("/api/users")).andExpect(status().isOk());

    }

    @Test
    void createUsers() throws Exception {
        Users users= new Users();
        List<UserRequest> userRequests= new ArrayList<>();
        UserRequest userRequest= new UserRequest();
        userRequest.setUserType(User.UserType.EMPLOYEE);
        userRequest.setUserEmail("sreeetejreddy19@gmail.com");
        userRequests.add(userRequest);
        users.setUsersList(userRequests);
        when(userService.createUsers(any(),any())).thenReturn(new ArrayList<>());
        mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(users)))
                .andExpect(status().isCreated());

    }

    @Test
    void updateUser() throws Exception {
        UserRequest userRequest= new UserRequest();
        userRequest.setUserType(User.UserType.EMPLOYEE);
        userRequest.setUserEmail("sreeetejreddy19@gmail.com");
        when(userService.updateUser(any(),any(),any())).thenReturn(new User());
        mockMvc.perform(put("/api/users/"+UUID.randomUUID().toString()).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk());

    }

    @Test
    void deleteUser() throws Exception {
        Mockito.doAnswer(r -> null).when(userService).deleteUser(any(),any());
        mockMvc.perform(delete("/api/users/"+UUID.randomUUID().toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
}