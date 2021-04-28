package com.aegis.usermanagement.controller;

import com.aegis.usermanagement.model.UserAction;
import com.aegis.usermanagement.service.UserActionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.UUID;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {UserActionController.class})
public class UserActionControllerTest {

    @Autowired
    ObjectMapper mapper;


    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserActionService userActionService;

    @Test
    public void createAction() throws Exception{
     when(userActionService.createUserAction(any(),any(),any())).thenReturn(new UserAction());
        mockMvc.perform(post("/api/users/"+UUID.randomUUID()+"/actions").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(new UserAction())))
                .andExpect(status().isCreated());
    }

    @Test
    public void getAllActions() throws Exception {
        when(userActionService.getAllActions(any())).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/api/users/"+UUID.randomUUID()+"/actions")).andExpect(status().isOk());

    }

    @Test
    public void getAction() throws Exception {
        when(userActionService.getAction(any(),any())).thenReturn(new UserAction());
        mockMvc.perform(get("/api/users/"+ UUID.randomUUID()+"/actions/"+UUID.randomUUID())).andExpect(status().isOk());
    }
}