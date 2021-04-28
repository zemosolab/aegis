package com.aegis.cardmanagement.modal;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

@Entity
@Table
@Getter
@Setter
public class CardAction {

    public enum ActionType {CREATE, DELETE, ASSIGN, UNASSIGN, BLOCK, UNBLOCK, EXTEND, UPDATE}

    public enum ActionStatus {COMPLETED, NOTCOMPLETE}

    public enum ActionCreationType {SYSTEMGENERATED, USERGENERATED}

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;


    @Column
    @Enumerated(EnumType.STRING)
    private ActionType actionType;


    @Column
    private String reason;


    @Column
    @Enumerated(EnumType.STRING)
    private ActionCreationType actionCreationType = ActionCreationType.USERGENERATED;


    @Column
    @Enumerated(EnumType.STRING)
    private ActionStatus actionStatus;


    @JsonIgnore
    @ManyToOne
    private Card card;

    @Column
    private HashMap<String, String> details;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column
    private Date createdAt;

    @Column
    private String createdBy;

}
