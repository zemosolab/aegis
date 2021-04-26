package com.aegis.eventmanagement.controller;

import com.aegis.eventmanagement.model.Event;
import com.aegis.eventmanagement.pojos.request.EventRequest;
import com.aegis.eventmanagement.service.EventService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(EventController.class)
public class EventControllersTest {
    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    MockMvc mvc;
    @MockBean
    EventService eventService;


    @Test
    public void createEvent() throws Exception {
        EventRequest eventRequest = new EventRequest("device", null);
        when(eventService.createEvent(Mockito.any(EventRequest.class)))
                .thenReturn(new Event(Event.EventType.ALARM, "device", null));
        mvc.perform(post("/api/events").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(eventRequest)))
                .andExpect(status().isCreated());
    }

    @Test
   public void getEvents() throws Exception {
        List<Event> eventList = new ArrayList<>();
        eventList.add(new Event(Event.EventType.OTHERS, "deviceId", null));
        when(eventService.getEvents(Mockito.anyString(), Mockito.any())).thenReturn(eventList);
        mvc.perform(get("/api/events").queryParam("type", Event.EventType.OTHERS.toString()).queryParam("deviceId", "deviceId"))
                .andExpect(status().isOk());

    }

    @Test
   public void getEvent() throws Exception {
        Event event = new Event(Event.EventType.OTHERS, "deviceId", null);
        when(eventService.getEventById(Mockito.anyString())).thenReturn(event);
        mvc.perform(get("/api/events/123123")).andExpect(status().isOk());
    }

    @Test
   public void deleteEvent() throws Exception {
        Mockito.doAnswer(r -> {
            return null;
        }).when(eventService).deleteEvent(any());
        mvc.perform(get("/api/events/123")).andExpect(status().isOk());
    }

    @Test
    public void deleteEvents() throws Exception {
        Mockito.doAnswer(r -> {
            return null;
        }).when(eventService).deleteEvents(any(), any());
        mvc.perform(get("/api/events").queryParam("type", Event.EventType.OTHERS.toString()).queryParam("deviceId", "deviceId"))
                .andExpect(status().isOk());

    }
}