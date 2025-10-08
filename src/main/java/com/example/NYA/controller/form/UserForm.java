package com.example.NYA.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalTime;

@Getter
@Setter
public class UserForm {

    private Integer id;

    private String account;

    private String password;

    private String name;

    private Integer departmentId;

    private Integer authority;

    private LocalTime workStart;

    private LocalTime workEnd;

    private LocalTime restStart;

    private LocalTime restEnd;

    private short isStopped;

    private Timestamp createdDate;

    private Timestamp updatedDate;

}
