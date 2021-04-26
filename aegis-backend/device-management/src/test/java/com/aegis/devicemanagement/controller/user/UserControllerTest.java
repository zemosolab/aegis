package com.aegis.devicemanagement.controller.user;

import com.aegis.devicemanagement.model.user.User;
import com.aegis.devicemanagement.pojos.request.UserRequest;
import com.aegis.devicemanagement.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Test
    public void createUser() throws Exception {
        User user = new User();
        user.setId("123");
        when(userService.createUser(Mockito.any(UserRequest.class), Mockito.any(),any())).thenReturn(user);
        mockMvc.perform(post("/api/devices/Nikhil/users").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(user)))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateUser() throws Exception {
        User user = new User();
        user.setId("Nikhil");
        when(userService.updateUser(Mockito.any(UserRequest.class), Mockito.any(String.class), Mockito.any(),anyString())).thenReturn(user);
        mockMvc.perform(put("/api/devices/Nikhil/users/123")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(user)))
                .andExpect(status().isAccepted());
    }

    @Test
    public void getAll() throws Exception {
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setId("Nikhil");
        users.add(user);
        when(userService.getAllUsersFromADevice(Mockito.any())).thenReturn(users);
        mockMvc.perform(get("/api/devices/Nikhil/users")).andExpect(status().isOk());
    }

    @Test
    public void getUser() throws Exception {
        User user = new User();
        user.setId("Nikhil");
        when(userService.getUser(Mockito.any(), Mockito.anyString())).thenReturn(user);
        mockMvc.perform(get("/api/devices/Nikhil/users/123")).andExpect(status().isOk());
    }

    @Test
    public void deleteUser() throws Exception {
        Mockito.doAnswer(r -> null).when(userService).deleteUser(any(), any(),any());
        mockMvc.perform(delete("/api/devices/Nikhil/users/123")).andExpect(status().isNoContent());
    }
}