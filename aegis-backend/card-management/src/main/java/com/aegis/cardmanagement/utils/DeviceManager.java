package com.aegis.cardmanagement.utils;

import com.aegis.cardmanagement.modal.CardAction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class DeviceManager {

    @Value(value = "${device-management.serviceUrl}")
    String deviceControllerUrl;

    @Value(value = "${user-management.serviceUrl}")
    String userManagementUrl;

    private ObjectMapper mapper;


    private final RestTemplate restTemplate;

    public DeviceManager() {
        this.restTemplate = new RestTemplate();
        mapper = new ObjectMapper();
    }

    public JsonNode retrieveUserInfo(UUID userId) throws JsonProcessingException {
        String userInfo = restTemplate.getForObject(userManagementUrl + userId, String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode user = mapper.readTree(userInfo);
        return user;
    }

    public void cardActionOnDevice(CardAction cardAction, String hardwareId, CardAction.ActionType actionType) throws JsonProcessingException {
        for (String deviceId : fetchAllDevices()) {
            String userId = cardAction.getDetails().get("userId");
            deviceControllerUrl = deviceControllerUrl.replace("DEVICE_ID", deviceId);
            if (actionType != CardAction.ActionType.EXTEND &&
                    actionType != CardAction.ActionType.UPDATE) {
                HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                String authHeader = httpServletRequest.getHeader("Authorization");
                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", authHeader);
                headers.setContentType(MediaType.APPLICATION_JSON);
                JSONObject userJson = new JSONObject();
                JSONObject deviceUser = new JSONObject();
                JsonNode userInfo = retrieveUserInfo(UUID.fromString(userId));
                deviceUser.put("userId", userInfo.get("controllerUserId"));
                deviceUser.put("refUserId", userInfo.get("userRef"));

                if (actionType == CardAction.ActionType.UNBLOCK || actionType == CardAction.ActionType.ASSIGN) {
                    deviceUser.put("card1", hardwareId);
                } else {
                    deviceUser.put("card1", "");
                }
                userJson.put("deviceUser", deviceUser);
                HttpEntity<String> request =
                        new HttpEntity<String>(userJson.toString(), headers);
                restTemplate.exchange(deviceControllerUrl + userInfo.get("deviceUserId").textValue(), HttpMethod.PUT, request, String.class);
            }
        }
    }

    public List<String> fetchAllDevices() throws JsonProcessingException {
        List<String> deviceIds = new ArrayList<>();
        String url = deviceControllerUrl;
        String devices = restTemplate.getForObject(url.substring(0, deviceControllerUrl.indexOf("/DEVICE_ID")), String.class);
        JsonNode jsonNode = mapper.readTree(devices);
        for (JsonNode device : jsonNode) {
            deviceIds.add(device.get("id").textValue());
        }
        return deviceIds;
    }


}
