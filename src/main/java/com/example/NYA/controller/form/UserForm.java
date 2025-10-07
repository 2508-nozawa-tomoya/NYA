package com.example.NYA.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.sql.Timestamp;

@Getter
@Setter
public class UserForm {

    private Integer id;

    private Integer account;

    private String password;

    private String name;

    private Integer departmentId;

    private int authority;

    private Time work_start;

    private Time work_end;

    private Time rest;

    private int isStopped;

    private Timestamp createDate;

    private Timestamp updatedDate;

}
