package com.aegis.eventmanagement.pojos.request;

import com.aegis.eventmanagement.pojos.eventInfo.Details;
import com.aegis.eventmanagement.pojos.eventInfo.ProcessedDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventRequest {

    String eventId;
    String deviceId;
    Details details;
    ProcessedDetails processedDetails;

    public EventRequest(String deviceId, Details details) {

    }

}
