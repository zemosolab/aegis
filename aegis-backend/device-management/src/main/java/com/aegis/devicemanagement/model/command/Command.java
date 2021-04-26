package com.aegis.devicemanagement.model.command;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashMap;

@Setter
@Getter
@Document("Command")
public class Command {
    @Id
    private String id;
    private String type;
    private String deviceId;
    private String createdBy;
    private Date createdAt;
    private HashMap<String,String> details;
}
