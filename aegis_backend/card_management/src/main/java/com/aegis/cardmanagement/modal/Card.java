package com.aegis.cardmanagement.modal;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name="card")

public class Card {
    public enum Status{ASSIGNED,BLOCKED,UNASSIGNED,UNBLOCKED};
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column
    private String hardwareId;

    @Column
    private UUID assignmentId;


    @Column
    private String cardName;

    @Column
    @Enumerated(EnumType.STRING)
    private Status cardStatus=Status.UNASSIGNED;

    @Column
    private boolean isDeleted=false;

    @JsonBackReference
    @OneToMany(mappedBy="card", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<CardAction> cardAuditList=new HashSet<>();




}
