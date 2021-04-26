package com.aegis.devicemanagement.model.device;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashMap;


@Setter
@Getter
@Document("DeviceAction")
public class DeviceAction {
    public enum ActionStatus {COMPLETED, NOTCOMPLETE};
    public enum ActionType {CREATE, DELETE, UPDATE};
    public enum ActionCreationType {SYSTEMGENERATED, USERGENERATED};


    @Id
    private String id;
    private ActionStatus status=ActionStatus.NOTCOMPLETE;
    private String reason;
    private ActionCreationType actionCreationType = ActionCreationType.USERGENERATED;
    private ActionType actionType;
    private Date createdAt;
    private String createdBy;
    private String deviceId;
    private HashMap<String,String> details;
}
