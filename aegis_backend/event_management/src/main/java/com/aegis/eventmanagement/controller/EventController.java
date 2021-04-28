package com.aegis.eventmanagement.controller;


import com.aegis.eventmanagement.exceptions.DeviceNotFound;
import com.aegis.eventmanagement.exceptions.EventNotFound;
import com.aegis.eventmanagement.exceptions.SystemException;
import com.aegis.eventmanagement.model.Event;
import com.aegis.eventmanagement.pojos.request.EventRequest;
import com.aegis.eventmanagement.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EventController {

    @Autowired
    EventService eventService;

    @PostMapping("/events")
    public ResponseEntity<Event> createEvent(@RequestBody EventRequest eventRequest) {
        Event event;
        try {
            event = eventService.createEvent(eventRequest);
        } catch (SystemException systemException) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "system exception", systemException);
        }
        return new ResponseEntity<Event>(event, HttpStatus.CREATED);
    }


    @GetMapping("/events")
    public ResponseEntity<List<Event>> getEvents(@RequestParam(required = false) Event.EventType type, @RequestParam(required = false) String deviceId) {
        List<Event> events;
        try {
            events = eventService.getEvents(deviceId, type);
        } catch (DeviceNotFound deviceNotFound) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "device not found", deviceNotFound);
        } catch (SystemException systemException) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "system exception", systemException);
        }
        return ResponseEntity.ok(events);
    }


    @GetMapping("/events/{eventId}")
    public ResponseEntity<Event> getEvent(@PathVariable String eventId) {
        Event event;
        try {
            event = eventService.getEventById(eventId);
        } catch (EventNotFound eventNotFound) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "event not found", eventNotFound);
        }
        return ResponseEntity.ok(event);
    }

    @DeleteMapping("/events")
    public ResponseEntity deleteEvent(@RequestParam("type") Event.EventType type, @RequestParam("deviceId") String deviceId) {
        try {
            eventService.deleteEvents(type, deviceId);
        } catch (DeviceNotFound deviceNotFound) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "device not found", deviceNotFound);
        } catch (SystemException systemException) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "system exception", systemException);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/events/{eventId}")
    public ResponseEntity deleteEvent(@PathVariable String eventId) {
        try {
            eventService.deleteEvent(eventId);
        } catch (EventNotFound eventNotFound) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "event not found", eventNotFound);
        }
        return new ResponseEntity(HttpStatus.OK);
    }


}
