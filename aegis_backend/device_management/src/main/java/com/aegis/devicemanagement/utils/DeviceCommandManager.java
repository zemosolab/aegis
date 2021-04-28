package com.aegis.devicemanagement.utils;

import com.aegis.devicemanagement.exception.device.DeviceNotFound;
import com.aegis.devicemanagement.exception.user.UserNotFound;
import com.aegis.devicemanagement.model.device.Device;
import com.aegis.devicemanagement.service.device.DeviceService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Component
public class DeviceCommandManager {

    @Qualifier("deviceActionMap")
    @Autowired
    Map<String, HashMap<String, String>> deviceActionMap;

    @Autowired
    private Map<String, String> classToDeviceMap;


    @Autowired
    private DeviceService deviceService;

    private final RestTemplate restTemplate;

    public DeviceCommandManager() {
        this.restTemplate = new RestTemplate();
    }

    public Map<String, String> httpRequestGenerator(Object object, Helper.ActionType action,String deviceId) throws DeviceNotFound {
        Gson gson = new Gson();
        String json = gson.toJson(object);
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        HashMap<String, String> httpRequestResponse = new HashMap<>();
        for (String k : jsonObject.keySet()) {
            if (jsonObject.get(k).isJsonObject()) {
                String actionType = classToDeviceMap.get(k);
                HashMap<String, String> queryParams = gson.fromJson(jsonObject.get(k), HashMap.class);
                sendRequest(actionType, queryParams, httpRequestResponse, action, deviceId);
            }
        }
        return httpRequestResponse;
    }


    public void sendRequest(String actionType, Map<String, String> queryParams, Map<String, String> httpRequestResponse, Helper.ActionType action,String deviceId) throws DeviceNotFound {
        String actionParam=deviceActionMap.get(actionType).get(action.toString());
        Device device=deviceService.getDevice(deviceId);
        String deviceLink=device.getDeviceLink();
        String matrix = deviceLink + actionType + "?action=" + actionParam.toLowerCase()+"&format=xml";
        matrix = new QueryString(matrix, queryParams).getQuery();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        headers.set("Authorization", "Basic " + device.getAuthKey());
        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<String> responseEntity = this.restTemplate.exchange(matrix, HttpMethod.GET, request, String.class);
        httpRequestResponse.put(actionType + "-response", responseEntity.toString());
        httpRequestResponse.put(actionType + "-request", matrix);
    }
}