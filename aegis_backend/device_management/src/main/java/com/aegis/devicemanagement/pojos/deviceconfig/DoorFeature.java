package com.aegis.devicemanagement.pojos.deviceconfig;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoorFeature {
    private String doorSense;
    private String exitSwitch;
    private String doorSenseActive;
    private String allowExitWhenLocked;
}
