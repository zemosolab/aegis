package com.aegis.devicemanagement.pojos.enrollmentConfig;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CardReadWrite {
    private String refUserId;
    private String userId;
    private String name;
    private String asc;
    private String bg;
    private String contact;
    private String accessLevel;
    private String cardNo;
}
