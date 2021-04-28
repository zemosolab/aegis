package com.aegis.usermanagement.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="users")
@Getter
@Setter
public class User {

    public enum UserType{GUEST,CONTRACTOR,EMPLOYEE}

    public enum Status{BLOCKED,ACTIVE}

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column
    private String username;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status=Status.ACTIVE;


    @Column
    @NotNull
    @Enumerated(EnumType.STRING)
    private UserType userType;


    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateOfJoining;


    @Column(unique = true)
    private String userEmail;

    @Column(unique = true)
    private String employeeId;

    @Column(unique = true)
    private String phoneNo;

    @Column(columnDefinition="serial")
    @Generated(GenerationTime.INSERT)
    private Long controllerUserId;

    @Column(columnDefinition="serial")
    @Generated(GenerationTime.INSERT)
    private Long userRef;


    @Column
    private String deviceUserId;

    @Column
    private boolean isDeleted=false;



    @JsonBackReference
    @OneToMany(mappedBy="user", cascade = CascadeType.REMOVE)
    private Set<UserAction> userAuditList=new HashSet<>();



}
