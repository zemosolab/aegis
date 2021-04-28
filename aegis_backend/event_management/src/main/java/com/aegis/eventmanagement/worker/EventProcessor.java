package com.aegis.eventmanagement.worker;

import com.aegis.eventmanagement.exceptions.SystemException;
import com.aegis.eventmanagement.model.Event;
import com.aegis.eventmanagement.pojos.eventInfo.ProcessedDetails;
import com.aegis.eventmanagement.pojos.request.EventRequest;
import com.aegis.eventmanagement.service.EventService;
import com.aegis.eventmanagement.utils.Helper;
import com.google.gson.Gson;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RabbitListener(queues = "deviceEvents")
public class EventProcessor {

    @Autowired
    private EventService eventService;

    private Gson gson;

    public EventProcessor() {
        gson = new Gson();
    }

    @Autowired
    Map<String, Event.EventType> eventDescMap;

    @RabbitHandler
    public void getEventFromQueue(String eventRequest) {
        EventRequest eventRequest1 = gson.fromJson(eventRequest, EventRequest.class);
        try {
            processEventByType(eventRequest1);
        } catch (SystemException systemException) {
            systemException.printStackTrace();
        }
    }

    public void processEventByType(EventRequest eventRequest) throws SystemException {

        String field1 = null, field2 = null, field3;
        if (eventDescMap.get(eventRequest.getEventId()) == Event.EventType.USER) {
            if (!eventRequest.getEventId().equals("111") || !eventRequest.getEventId().equals("171"))
                field1 = eventRequest.getDetails().getDetail1();
            if (!eventRequest.getEventId().equals("112") || !eventRequest.getEventId().equals("165"))
                field2 = eventRequest.getDetails().getDetail2();
            field3 = Integer.toBinaryString(Integer.parseInt(eventRequest.getDetails().getDetail3()));
            field3 = Helper.appendZeros(field3);
        } else {
            field1 = eventRequest.getDetails().getDetail1();
            field2 = eventRequest.getDetails().getDetail2();
            field3 = eventRequest.getDetails().getDetail3();
        }
        Map<String, String> map1 = new HashMap<>();
        map1.put("userId", field1);
        Map<String, String> map2 = new HashMap<>();
        map2.put("splFuncValue", field2);
        Map<String, String> map3 = new HashMap<>();
        map3.put("access", field3);
        ProcessedDetails processedDetails = new ProcessedDetails();
        processedDetails.setDetail1(map1);
        processedDetails.setDetail2(map2);
        processedDetails.setDetail3(map3);
        eventRequest.setProcessedDetails(processedDetails);
        eventService.createEvent(eventRequest);
    }


}