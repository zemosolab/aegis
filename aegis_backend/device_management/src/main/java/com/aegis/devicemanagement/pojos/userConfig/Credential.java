package com.aegis.devicemanagement.pojos.userConfig;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Credential {
    private String type; //here we only use the Card to access. Available options:- ( Finger, Card, Palm, Palm template with guide mode)
    private String card1;
}
