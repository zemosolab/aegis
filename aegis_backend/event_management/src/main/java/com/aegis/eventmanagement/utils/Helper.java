package com.aegis.eventmanagement.utils;

import com.aegis.eventmanagement.model.Device;
import com.aegis.eventmanagement.model.Event;
import com.aegis.eventmanagement.pojos.eventInfo.Details;
import com.aegis.eventmanagement.pojos.request.DeviceRequest;
import com.aegis.eventmanagement.pojos.request.EventRequest;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;


@Component
@Slf4j
public class Helper {


    private static String deviceUrlParams = "events?action=getevent&roll-over-count=ROLL_OVER_COUNT&seq-number=SEQ_NO&no-of-events=5&format=xml";

    public static Event copyEventDetails(Event event, EventRequest eventRequest) {
        if (eventRequest.getDetails() != null)
            event.setDetails(eventRequest.getDetails());
        if (eventRequest.getProcessedDetails() != null)
            event.setProcessedDetails(eventRequest.getProcessedDetails());
        if (eventRequest.getDeviceId() != null)
            event.setDeviceId(eventRequest.getDeviceId());
        if(eventRequest.getEventId()!=null)
            event.setEventId(eventRequest.getEventId());

        return event;
    }

    public static Device copyDeviceDetails(Device device, DeviceRequest deviceRequest){
        if(deviceRequest.getAuthKey()!=null)
            device.setAuthKey(deviceRequest.getAuthKey());
        if(deviceRequest.getDeviceId()!=null)
            device.setDeviceId(deviceRequest.getDeviceId());
        if(deviceRequest.getDeviceLink()!=null)
            device.setDeviceLink(deviceRequest.getDeviceLink());
        return device;
    }

    @Bean
    public Map<String, Event.EventType> eventDescMap() {
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

        return eventDescMap;
    }

    public static boolean doesObjectContainField(Object object, String fieldName) {
        return Arrays.stream(object.getClass().getDeclaredFields())
                .anyMatch(f -> f.getName().equals(fieldName));
    }

    public static String generateDeviceUri(String deviceLink, String seqNo, String rollOverCount) {
        String deviceEventLink = deviceLink + deviceUrlParams;
        deviceEventLink = deviceEventLink.replace("SEQ_NO", seqNo);
        deviceEventLink = deviceEventLink.replace("ROLL_OVER_COUNT", rollOverCount);
        return deviceEventLink;
    }

    public static String convertToCamelcase(String string) {
        StringBuilder camelCaseField = new StringBuilder();
        boolean toCapitalize = false;
        for (char a : string.toCharArray()) {
            if (toCapitalize) {
                if (Character.isAlphabetic(a)) {
                    camelCaseField.append(Character.toUpperCase(a));
                } else camelCaseField.append(a);
                toCapitalize = false;
            } else if (a == '-') {
                toCapitalize = true;
            } else camelCaseField.append(a);
        }
        return camelCaseField.toString();
    }

    public static List<EventRequest> convertXmlToEvent(String xml) {
        List<EventRequest> eventRequestList = new ArrayList<>();
        JSONObject jsonObj = XML.toJSONObject(xml);
        JSONArray jsonArray = jsonObj.getJSONObject("COSEC_API").getJSONArray("Events");
        jsonArray.forEach(object -> {
            JSONObject json = ((JSONObject) object);
            Details details = new Details();
            String eventId = null;
            Iterator<String> fieldNames = json.keys();
            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                String value = json.get(fieldName).toString();
                fieldName = convertToCamelcase(fieldName);
                if (fieldName.equals("eventId")) eventId = value;
                Class detailClass = details.getClass();

                if (doesObjectContainField(details, fieldName)) {
                    Field field = null;
                    try {
                        field = detailClass.getDeclaredField(fieldName);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                    field.setAccessible(true);
                    try {
                        field.set(details, value);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            EventRequest event = new EventRequest();
            event.setEventId(eventId);
            event.setDetails(details);
            eventRequestList.add(event);
        });
        return eventRequestList;
    }


    public static String appendZeros(String binary) {
        int len = 16 - binary.length();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            stringBuilder.append(0);
        }
        stringBuilder.append(binary);
        return stringBuilder.toString();
    }
}
