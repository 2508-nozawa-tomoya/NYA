package com.example.NYA.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.sql.Timestamp;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Integer account;

    @Column
    private String password;

    @Column
    private String name;

    @Column
    private Integer departmentId;

    @Column
    private int authority;

    @Column
    private Time workStart;

    @Column
    private Time workEnd;

    @Column
    private Time rest;

    @Column
    private int isStopped;

    @Column(insertable = false, updatable = false)
    private Timestamp createDate;

    @Column(insertable = false, updatable = true)
    private Timestamp updatedDate;

}
