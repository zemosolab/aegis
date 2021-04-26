package com.aegis.cardmanagement.controller;

import com.aegis.cardmanagement.modal.CardAction;
import com.aegis.cardmanagement.pojos.CardActionRequest;
import com.aegis.cardmanagement.service.CardActionService;
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
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CardActionController.class)
public class CardActionControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CardActionService cardActionService;

    @Test
    public void doAction() throws Exception {
        CardAction cardAction = new CardAction();
        cardAction.setReason("RandomCard");
        when(cardActionService.createCardAction(any(), Mockito.any(CardActionRequest.class), any())).thenReturn(cardAction);
        mockMvc.perform(post("/api/cards/" + UUID.randomUUID() + "/actions").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cardAction))).andExpect(status().isOk());
    }

    @Test
    public void getActions() throws Exception {
        List<CardAction> cardActions = new ArrayList<>();
        CardAction cardAction = new CardAction();
        cardAction.setReason("123");
        cardActions.add(cardAction);
        when(cardActionService.getAllActions(Mockito.any())).thenReturn(cardActions);
        mockMvc.perform(get("/api/cards/" + UUID.randomUUID() + "/actions")).andExpect(status().isOk());
    }

    @Test
    public void getAction() throws Exception {
        CardAction cardAction = new CardAction();
        cardAction.setReason("RandomCard");
        when(cardActionService.getActionById(Mockito.any(), Mockito.any())).thenReturn(cardAction);
        mockMvc.perform(get("/api/cards/" + UUID.randomUUID() + "/actions/" + UUID.randomUUID())).andExpect(status().isOk());
    }
}