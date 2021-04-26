package com.aegis.usermanagement.utils;


import com.aegis.usermanagement.exception.UserNotFound;
import com.aegis.usermanagement.model.User;
import com.aegis.usermanagement.model.UserAction;
import com.aegis.usermanagement.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@Component
public class DeviceManager {

    @Value(value = "${device-management.serviceUrl}")
    String deviceControllerUrl;

    private final RestTemplate restTemplate;

    private  ObjectMapper mapper;

    @Autowired
    private UserService userService;


    public DeviceManager() {
        this.restTemplate = new RestTemplate();
        mapper= new ObjectMapper();
    }

    public void registerUserWithDevice(User user,UserAction userAction) throws JsonProcessingException, UserNotFound {
        for(String deviceId : fetchAllDevices()) {
            String url =deviceControllerUrl.replace("DEVICE_ID",deviceId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            JSONObject userJson = new JSONObject();
            JSONObject deviceUser = new JSONObject();
            deviceUser.put("userId", user.getControllerUserId());
            deviceUser.put("refUserId", user.getUserRef());
            userJson.put("deviceUser", deviceUser);
            HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String authHeader = httpServletRequest.getHeader("Authorization");
            headers.add("Authorization", authHeader );
            HttpEntity<String> request =
                    new HttpEntity<String>(userJson.toString(), headers);
            String enrolledUser = restTemplate.postForObject(url, request, String.class);
            JsonNode jsonNode = mapper.readTree(enrolledUser);
            String deviceUserId = jsonNode.get("id").textValue();
            userService.updateDeviceUserId(user, deviceUserId);
        }

    }

    public void deleteUserFromDevice(User user,UserAction userAction){
        String deviceId=userAction.getDetails().get("deviceControllerId");
        deviceControllerUrl=deviceControllerUrl.replace("DEVICE_ID",deviceId);
        HttpHeaders headers = new HttpHeaders();
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authHeader = httpServletRequest.getHeader("Authorization");
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", authHeader );
        HttpEntity<String> request =
                new HttpEntity<String>( headers);
        restTemplate.exchange(deviceControllerUrl+user.getControllerUserId(), HttpMethod.DELETE, request, Void.class);
    }

    public List<String> fetchAllDevices() throws JsonProcessingException {
        List<String> deviceIds = new ArrayList<>();
        String url = deviceControllerUrl;
        String devices = restTemplate.getForObject(url.substring(0, deviceControllerUrl.indexOf("/DEVICE_ID")), String.class);
        JsonNode jsonNode = mapper.readTree(devices);
        for(JsonNode device : jsonNode) {
            deviceIds.add(device.get("id").textValue());
        }
        return deviceIds;
    }
}
