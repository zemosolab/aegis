package com.aegis.devicemanagement.model.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashMap;

@Getter
@Setter
@Document("UserAction")
public class UserAction {
    public enum ActionStatus {COMPLETED, INCOMPLETE};
    public enum ActionType {CREATE, DELETE, UPDATE};
    public enum ActionCreationType {SYSTEMGENERATED, USERGENERATED};


    @Id
    private String id;
    private UserAction.ActionStatus status= UserAction.ActionStatus.INCOMPLETE;
    private String reason;
    private UserAction.ActionCreationType actionCreationType = UserAction.ActionCreationType.USERGENERATED;
    private UserAction.ActionType actionType;
    private Date createdAt;
    private String createdBy;
    private String userId;
    private HashMap<String,String> details;
}
