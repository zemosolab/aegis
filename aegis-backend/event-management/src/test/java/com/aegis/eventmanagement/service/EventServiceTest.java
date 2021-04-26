package com.aegis.eventmanagement.service;

import com.aegis.eventmanagement.helper.EventStore;
import com.aegis.eventmanagement.helper.Helper;
import com.aegis.eventmanagement.model.Event;
import com.aegis.eventmanagement.pojos.request.EventRequest;
import com.aegis.eventmanagement.repository.event.EventRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class EventServiceTest {


    @Mock
    EventRepository eventRepository;

    EventService eventService;

    EventStore eventStore = new EventStore();


    @Before
    public void init() {
        Mockito.when(eventRepository.save(any())).thenAnswer(r -> {
            Event card = r.getArgument(0);
            return eventStore.addToStore(card);
        });

        Mockito.when(eventRepository.findByTypeAndDeviceId(any(), any())).thenAnswer(r -> {
            String type = r.getArgument(0);
            String deviceId = r.getArgument(1);
            return eventStore.findByDeviceAndType(deviceId, type);
        });

        Mockito.when(eventRepository.findById(any())).thenAnswer(r -> {
            String uuid = r.getArgument(0);
            return Optional.ofNullable(eventStore.getFromStore(uuid));
        });

        Mockito.when(eventRepository.findByDeviceId(any())).thenAnswer(r -> {
            String uuid = r.getArgument(0);
            return eventStore.findByDeviceId(uuid);
        });
        Mockito.doAnswer(r -> {
            eventStore.delete(r.getArgument(0));
            return null;
        }).when(eventRepository).deleteById(any());

        Mockito.doAnswer(r -> {
            eventStore.deleteByTypeAndDeviceId(r.getArgument(0), r.getArgument(1));
            return null;
        }).when(eventRepository).deleteByTypeAndDeviceId(any(), any());

        eventService = new EventService();
        eventService.setEventRepository(eventRepository);
        Map<String, Event.EventType> eventDescMap = new HashMap<>();
        for (int i = 101; i < 112; i++) {
            eventDescMap.put(Integer.toString(i), Event.EventType.USER);
        }
        for (int i = 151; i < 172; i++) {
            eventDescMap.put(Integer.toString(i), Event.EventType.USERDENIED);
        }
        for (int i = 201; i <= 209; i++) {
            if (i == 207) continue;
            eventDescMap.put(Integer.toString(i), Event.EventType.DOOR);
        }
        for (int i = 303; i <= 329; i++) {
            eventDescMap.put(Integer.toString(i), Event.EventType.ALARM);
        }
        for (int i = 351; i <= 353; i++) {
            eventDescMap.put(Integer.toString(i), Event.EventType.ALARM);
        }
        for (int i = 401; i <= 413; i++) {
            eventDescMap.put(Integer.toString(i), Event.EventType.SYSTEM);
        }
        for (int i = 451; i <= 461; i++) {
            eventDescMap.put(Integer.toString(i), Event.EventType.SYSTEM);
        }
        for (int i = 501; i <= 512; i++) {
            eventDescMap.put(Integer.toString(i), Event.EventType.CAFETERIA);
        }
        eventService.setEventDescMap(eventDescMap);

    }


    @Test
    public void createEventTest() {
        EventRequest eventRequest = (EventRequest) Helper.populate(new EventRequest(), EventRequest.class);
        eventRequest.setEventId("101");
        try {
            Event event = eventService.createEvent(eventRequest);
            List<Event> eventList = eventStore.findByDeviceId(eventRequest.getDeviceId());
            Assert.assertEquals(eventRequest.getDeviceId(), event.getDeviceId());
            Assert.assertEquals(eventList.get(0).getType(), event.getType());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    @Test
    public void deleteEventTest() {
        EventRequest eventRequest = (EventRequest) Helper.populate(new EventRequest(), EventRequest.class);
        eventRequest.setEventId("101");
        try {
            Event event = eventService.createEvent(eventRequest);
            eventService.deleteEvent(event.getId());
            Assert.assertEquals(eventStore.getAllFromStore().size(), 0);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    @Test
    public void deleteEventsTest() {
        EventRequest eventRequest = (EventRequest) Helper.populate(new EventRequest(), EventRequest.class);
        eventRequest.setEventId("101");
        try {
            Event event = eventService.createEvent(eventRequest);
            eventService.deleteEvents(event.getType(), event.getDeviceId());
            Assert.assertEquals(eventStore.getAllFromStore().size(), 0);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Test
    public void getEvent() {
        EventRequest eventRequest = (EventRequest) Helper.populate(new EventRequest(), EventRequest.class);
        eventRequest.setEventId("101");
        try {
            Event event = eventService.createEvent(eventRequest);
            Assert.assertEquals(eventService.getEventById(event.getId()).getDeviceId(), event.getDeviceId());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Test
    public void getEvents() {
        EventRequest eventRequest = (EventRequest) Helper.populate(new EventRequest(), EventRequest.class);
        eventRequest.setEventId("101");
        try {
            Event event = eventService.createEvent(eventRequest);
            Assert.assertEquals(
                    eventService.getEvents(event.getDeviceId(), event.getType()).get(0).getDeviceId(), event.getDeviceId());
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }


}
