package com.aegis.devicemanagement.pojos.request;

import com.aegis.devicemanagement.pojos.userConfig.Credential;
import com.aegis.devicemanagement.pojos.userConfig.DeviceUser;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRequest {
    private String deviceId;
    private String name;
    private DeviceUser deviceUser;
    private Credential credential;
}
