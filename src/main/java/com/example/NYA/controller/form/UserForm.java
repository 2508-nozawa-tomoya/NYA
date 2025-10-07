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
    private Integer account;
    private String password;
    private String name;
    private Integer departmentId;
    private Integer authority;
    private LocalTime workStart;
    private LocalTime workEnd;
    private Duration rest;
    private Integer isStopped;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
