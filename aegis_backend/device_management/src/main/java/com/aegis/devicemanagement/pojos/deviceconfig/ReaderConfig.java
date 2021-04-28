package com.aegis.devicemanagement.pojos.deviceconfig;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReaderConfig {
    private String reader;
    private String doorAccessMode;
    private String doorEntryExitMode;
    private String tagReDetectDelay;
}
