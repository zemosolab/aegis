package com.aegis.devicemanagement.model.device;

import com.aegis.devicemanagement.pojos.deviceconfig.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Document("Device")
public class Device {
    @Id
    private String id;
    @NotEmpty
    @Indexed(unique = true)
    private String ipAddress;
    @NotEmpty
    private String portNo;
    private String authKey;
    private String deviceLink;
    private DeviceBasicConfig deviceBasicConfig;
    private Alarm alarm;
    private DateTime dateTime;
    private DoorFeature doorFeature;
    private Enrollment enrollment;
    private ReaderConfig readerConfig;
    private TCPEvent tcpEvent;
    private boolean isDeleted=false;
}

