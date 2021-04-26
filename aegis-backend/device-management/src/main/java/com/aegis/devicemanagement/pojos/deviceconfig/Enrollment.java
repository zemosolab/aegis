package com.aegis.devicemanagement.pojos.deviceconfig;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Enrollment {
    private String enrollOnDevice;
    private String enrollUsing;
    private String enrollMode;
    private String selfEnrollableEnable;
    private String selfEnrollableRetryCount;
}
