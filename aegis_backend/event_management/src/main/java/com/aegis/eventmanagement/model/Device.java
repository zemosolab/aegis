package com.aegis.eventmanagement.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Document("RegisteredDevice")
@Setter
@Getter
public class Device {

    @Id
    private String id;
    private String deviceId;
    @Indexed(unique = true)
    private String deviceLink;
    private String authKey;
    private int rollOverCount = 0;
    private long seqNo = 1L;
    private boolean isDeleted=false;
}
