package com.example.NYA.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

@Entity
@Table(name = "attendances")
@Getter
@Setter
public class Attendance {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Integer userId;

    @Column
    private Date workDate;

    @Column
    private Time startTime;

    @Column
    private Time endTime;

    @Column
    private Time rest;

    @Column
    private int status;

    @Column
    private String comment;

    @Column(insertable = false, updatable = false)
    private Timestamp createDate;

    @Column(insertable = false, updatable = true)
    private Timestamp updatedDate;

}
