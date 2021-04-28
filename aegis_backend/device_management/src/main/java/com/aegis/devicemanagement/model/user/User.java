package com.aegis.devicemanagement.model.user;

import com.aegis.devicemanagement.pojos.userConfig.Credential;
import com.aegis.devicemanagement.pojos.userConfig.DeviceUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document("User")
public class User {
    @Id
    private String id;
    private String name;
    private String deviceId;
    private DeviceUser deviceUser;
    private Credential credential;
    private boolean isDeleted=false;
}
