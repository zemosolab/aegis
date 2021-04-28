package com.aegis.eventmanagement.pojos.request;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DeviceRequest {
    private String deviceId;
    private String deviceLink;
    private String authKey;
}
