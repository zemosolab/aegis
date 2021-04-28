package com.aegis.eventmanagement.service;


import com.aegis.eventmanagement.exceptions.DeviceNotFound;
import com.aegis.eventmanagement.exceptions.EventNotFound;
import com.aegis.eventmanagement.exceptions.SystemException;
import com.aegis.eventmanagement.model.Event;
import com.aegis.eventmanagement.pojos.request.EventRequest;
import com.aegis.eventmanagement.repository.event.EventRepository;
import com.aegis.eventmanagement.utils.Helper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Setter
@Getter
@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    Map<String, Event.EventType> eventDescMap;

    public Event createEvent(EventRequest eventRequest) throws SystemException {
        Event event1;

        try {
            Event event = new Event();
            Helper.copyEventDetails(event, eventRequest);
            setEventType(event, event.getEventId());
            event1 = eventRepository.save(event);
        } catch (Exception systemException) {
            throw new SystemException("system Exception");
        }
        return event1;
    }


    public List<Event> getEvents(String deviceId, Event.EventType type) throws SystemException, DeviceNotFound {
        if (deviceId != null) checkIfDeviceExists(deviceId);
        List<Event> events;
        try {
            if (deviceId == null && type == null)
                events = eventRepository.findAllEvents();
            else if (type == null)
                events = eventRepository.findByDeviceId(deviceId);
            else if (deviceId == null)
                events = eventRepository.findByType(type);
            else
                events = eventRepository.findByTypeAndDeviceId(type.toString(), deviceId);

        } catch (Exception systemException) {
            throw new SystemException("system Exception");
        }
        return events;
    }

    public Event getEventById(String eventId) throws EventNotFound {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (!optionalEvent.isPresent()) {
            throw new EventNotFound("event not found");
        }
        Event event = optionalEvent.get();
        return event;
    }

    public void deleteEvent(String eventId) throws EventNotFound {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (!optionalEvent.isPresent()) {
            throw new EventNotFound("event not found");
        }
        eventRepository.deleteById(eventId);
    }

    public void deleteEvents(Event.EventType type, String deviceId) throws SystemException, DeviceNotFound {
        checkIfDeviceExists(deviceId);
        try {
            eventRepository.deleteByTypeAndDeviceId(type, deviceId);
        } catch (Exception systemException) {
            throw new SystemException("system exception");
        }
    }

    public void checkIfDeviceExists(String deviceId) throws DeviceNotFound {
        List<Event> events = eventRepository.findByDeviceId(deviceId);
        if (events.size() == 0) {
            throw new DeviceNotFound("device not found");
        }
    }

    public void setEventType(Event event, String eventId) {
        if (eventDescMap.containsKey(eventId)) {
            System.out.println(eventId);
            event.setType(eventDescMap.get(eventId));
        } else event.setType(Event.EventType.OTHERS);
    }


}
