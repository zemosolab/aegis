package com.aegis.eventmanagement.model;


import com.aegis.eventmanagement.pojos.eventInfo.Details;
import com.aegis.eventmanagement.pojos.eventInfo.ProcessedDetails;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Event")
@Setter
@Getter
public class Event {
    public enum EventType {USER, USERDENIED, ALARM, DOOR, OTHERS,SYSTEM,CAFETERIA}

    @Id
    private String id;
    private String eventId;
    private EventType type;
    private String deviceId;
    private Details details;
    private ProcessedDetails processedDetails;
    private boolean isDeleted = false;

    public Event(EventType type, String deviceId, Details details) {
        this.type = type;
        this.deviceId = deviceId;
        this.details = details;
    }
    public Event() {
    }
}
