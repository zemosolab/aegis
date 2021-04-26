package com.aegis.devicemanagement.pojos.deviceconfig;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TCPEvent {
    private String trigger;
    private String keepLiveEvents;
    private String ipAddress;
    private String port;
    private String rollOverCount;
    private String seqNumber;
    private String responseTime;
    private String Interface;
}
