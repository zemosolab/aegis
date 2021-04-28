package com.aegis.devicemanagement.pojos.request;

import com.aegis.devicemanagement.pojos.deviceconfig.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DeviceRequest {

    private String ipAddress;
    private String portNo;
    private String authKey;
    private DeviceBasicConfig deviceBasicConfig;
    private Alarm alarm;
    private DateTime dateTime;
    private DoorFeature doorFeature;
    private Enrollment enrollment;
    private ReaderConfig readerConfig;
    private TCPEvent tcpEvent;
}
