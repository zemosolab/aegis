package com.aegis.eventmanagement.repository.event;

import com.aegis.eventmanagement.model.Event;

public interface EventCustomizedRepository<T, ID> {
    void deleteById(ID id);

    void deleteByTypeAndDeviceId(Event.EventType type, String deviceId);
}
