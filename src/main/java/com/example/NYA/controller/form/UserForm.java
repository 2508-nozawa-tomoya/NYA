package com.example.NYA.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class UserForm {
    private Integer id;
    private String account;
    private String password;
    private String name;
    private Integer departmentId;
    private Short authority;
    private LocalTime workStart;
    private LocalTime workEnd;
    private LocalTime restStart;
    private LocalTime restEnd;
    private Short isStopped;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
