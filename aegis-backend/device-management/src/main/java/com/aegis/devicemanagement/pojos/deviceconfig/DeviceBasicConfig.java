package com.aegis.devicemanagement.pojos.deviceconfig;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeviceBasicConfig {
    private String name;
    private String generateInvalidUserEvents;
    private String generateExitSwitchEvents;
    private String manualModeDoorSelection;
}
