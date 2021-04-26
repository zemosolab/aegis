package com.aegis.eventmanagement.helper;

import com.aegis.eventmanagement.model.Event;

import java.util.*;
import java.util.stream.Collectors;

public class EventStore {
    private Map<String, Event> eventStore = new HashMap<>();

    public Event addToStore(Event card) {

        String uuid = UUID.randomUUID().toString();
        card.setId(uuid);
        eventStore.put(uuid, card);
        return eventStore.get(uuid);
    }

    public Event getFromStore(String uuid) {
        return eventStore.get(uuid);
    }

    public List<Event> getAllFromStore() {
        return new ArrayList<>(eventStore.values());
    }

    public void delete(String uuid) {
        if (eventStore.containsKey(uuid)) {
            eventStore.remove(uuid);
        }
    }


    public void deleteByTypeAndDeviceId(String type, String deviceId) {
        getAllFromStore().stream().forEach(event -> {
            if (event.getType().equals(type) && event.getDeviceId().equals(deviceId))
                delete(event.getId());
        });
    }


    public List<Event> findByDeviceId(String deviceId) {
        return getAllFromStore().stream()
                .filter(event -> event.getDeviceId().equals(deviceId))
                .collect(Collectors.toList());
    }

    public List<Event> findByDeviceAndType(String deviceId, String type) {
        return getAllFromStore().stream()
                .filter(event -> event.getDeviceId().equals(deviceId))
                .filter(event -> event.getType().equals(type))
                .collect(Collectors.toList());
    }

    public void clear() {
        eventStore = new HashMap<>();
    }
}
