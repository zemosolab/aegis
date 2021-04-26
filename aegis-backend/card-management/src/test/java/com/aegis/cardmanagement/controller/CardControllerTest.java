package com.aegis.cardmanagement.controller;

import com.aegis.cardmanagement.modal.Card;
import com.aegis.cardmanagement.pojos.CardRequest;
import com.aegis.cardmanagement.service.CardService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CardController.class)
public class CardControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CardService cardService;

    @Test
    public void createCard() throws Exception {
        Card card = new Card();
        card.setCardName("RandomCard");
        when(cardService.createCard(Mockito.any(CardRequest.class), any())).thenReturn(card);
        mockMvc.perform(post("/api/cards").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(card))).andExpect(status().isCreated());
    }

    @Test
    public void getCard() throws Exception {
        Card card = new Card();
        card.setCardName("RandomCard");
        when(cardService.getCard(any())).thenReturn(card);
        mockMvc.perform(get("/api/cards/" + UUID.randomUUID())).andExpect(status().isOk());
    }

    @Test
    public void getAllCards() throws Exception {
        List<Card> cards = new ArrayList<>();
        Card card = new Card();
        card.setCardName("RandomCard");
        cards.add(card);
        when(cardService.getAllCards()).thenReturn(cards);
        mockMvc.perform(get("/api/cards")).andExpect(status().isOk());
    }

    @Test
    public void updateCard() throws Exception {
        Card card = new Card();
        card.setCardName("RandomCard");
        when(cardService.updateCard(Mockito.any(CardRequest.class), any(), any())).thenReturn(card);
        mockMvc.perform(put("/api/cards/" + UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(card)))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteCard() throws Exception {
        Mockito.doAnswer(r -> null).when(cardService).deleteCard(any(), any());
        mockMvc.perform(delete("/api/cards/" + UUID.randomUUID())).andExpect(status().isOk());
    }

    @Test
    public void health() throws Exception {
        mockMvc.perform(get("/api/health/liveness")).andExpect(status().isOk());
    }
}