package com.example.NYA.controller.form;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.Interval;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import static com.example.NYA.error.ErrorMessages.*;

@Getter
@Setter
public class AttendanceForm {

    private Integer id;

    private Integer userId;

    @NotNull(message = E0004)
    private Date workDate;

    @NotNull(message = E0005)
    private Time startTime;

    @NotNull(message = E0006)
    private Time endTime;

    @NotNull(message = E0007)
    private Interval rest;

    private int status;

    private String comment;

    private Timestamp createDate;

    private Timestamp updatedDate;

}
