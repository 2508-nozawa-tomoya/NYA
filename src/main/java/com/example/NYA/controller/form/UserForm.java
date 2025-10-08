package com.example.NYA.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.sql.Timestamp;

@Getter
@Setter
public class UserForm {

    private Integer id;

    private String account;

    private String password;

    private String name;

    private Integer departmentId;

    private short authority;

    private Time workStart;

    private Time workEnd;

    private Time rest;

    private short isStopped;

    private Timestamp createdDate;

    private Timestamp updatedDate;

}
