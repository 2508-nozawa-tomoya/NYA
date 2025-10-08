package com.example.NYA.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class AttendanceForm {
    private Integer id;
    private Integer userId;
    private LocalDate workDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime startRest;
    private LocalTime endRest;
    private Integer status;
    private String comment;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
