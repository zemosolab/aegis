package com.aegis.eventmanagement.scheduler;

import com.aegis.eventmanagement.model.Device;
import com.aegis.eventmanagement.pojos.request.EventRequest;
import com.aegis.eventmanagement.service.DeviceService;
import com.aegis.eventmanagement.utils.Helper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class EventScheduler {

    private RestTemplate restTemplate;

    private Gson gson;

    @Autowired
    private DeviceService deviceService;


    @Autowired
    private RabbitTemplate template;

    @Autowired
    private Queue queue;



    public EventScheduler() {
        restTemplate = new RestTemplate();
        gson = new Gson();

    }


    @Scheduled(fixedDelay = 10000, initialDelay = 500)
    public void getEventsFromDevice() throws Exception {

        List<Device> devices = deviceService.getAllDevices();
        log.info("Devices IP_ADDRESS AND PORT fetched");
        for (Device device : devices) {
            String deviceUrl = Helper.generateDeviceUri(device.getDeviceLink(), Long.toString(device.getSeqNo()), Integer.toString(device.getRollOverCount()));
            log.info("generated link" + deviceUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_XML);
            headers.set("Authorization", "Basic " + device.getAuthKey());
            HttpEntity request = new HttpEntity(headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(deviceUrl, HttpMethod.GET, request, String.class);
            log.info("Fetching events from  :" + deviceUrl);
            List<EventRequest> eventRequestList = Helper.convertXmlToEvent(responseEntity.getBody());
            deviceService.updateEventCounter(device.getDeviceId(), eventRequestList.size());
            eventRequestList.forEach(eventRequest -> {
                String json = gson.toJson(eventRequest);
                this.template.convertAndSend(queue.getName(), json);
                log.info("Event sent to RabbitMQ" + json);
            });

        }


    }


}
