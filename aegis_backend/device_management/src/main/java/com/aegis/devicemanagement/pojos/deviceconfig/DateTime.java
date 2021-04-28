package com.aegis.devicemanagement.pojos.deviceconfig;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DateTime {
    private String timeFormat;
    private String updateMode;
    private String ntpServerType;
    private String timeZone;
    private String ntpServer;
    private boolean dstEnable;
}
