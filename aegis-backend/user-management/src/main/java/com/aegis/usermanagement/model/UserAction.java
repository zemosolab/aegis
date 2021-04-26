package com.aegis.usermanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

@Entity
@Table
@Getter
@Setter
public class UserAction {

    public enum ActionType{CREATE,REMOVE,BLOCK,UNBLOCK,UPDATE}
    public enum ActionStatus{COMPLETE,INCOMPLETE,REVERTED}

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;


    @Column
    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    @Column
    private String reason;

    @Column(columnDefinition="TEXT")
    private HashMap<String ,String > details;

    @Column
    private String createdBy;

    @Column
    private Date createdAt;

    @Column
    @Enumerated(EnumType.STRING)
    private ActionStatus status;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    @JoinColumn
    @JsonManagedReference
    private User user;



}
