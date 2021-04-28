package com.aegis.devicemanagement.controller.command;

import com.aegis.devicemanagement.model.command.Command;
import com.aegis.devicemanagement.service.command.CommandService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CommandController.class)
public class CommandControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CommandService commandService;

    @Test
    public void sendCommand() throws Exception {
        Command command = new Command();
        when(commandService.sendCommandToDevice(any(), any(), any())).thenReturn(command);
        mockMvc.perform(post("/api/devices/Card1/command").queryParam("commandType","clearalarm").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(command)))
                .andExpect(status().isCreated());
    }

    @Test
    public void getCommandInfo() throws Exception {
        Command command = new Command();
        when(commandService.sendCommandToDevice(any(), any(), any())).thenReturn(command);
        mockMvc.perform(get("/api/devices/Card1/command").queryParam("commandType","getcount"))
                .andExpect(status().isOk());
    }
}