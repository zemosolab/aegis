package com.aegis.devicemanagement.model.enrollment;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashMap;

@Setter
@Getter
@Document("EnrollmentAction")
public class EnrollmentAction {
    public enum ActionStatus {COMPLETED, INCOMPLETE};
    public enum ActionType {CREATE, UPDATE, DELETE};
    public enum ActionCreationType {SYSTEMGENERATED, USERGENERATED};

    @Id
    private String id;
    private EnrollmentAction.ActionStatus status= EnrollmentAction.ActionStatus.INCOMPLETE;
    private String reason;
    private EnrollmentAction.ActionCreationType actionCreationType = EnrollmentAction.ActionCreationType.USERGENERATED;
    private EnrollmentAction.ActionType actionType;
    private Date createdAt;
    private String createdBy;
    private String enrollmentId;
    private HashMap<String,String> details;
}
