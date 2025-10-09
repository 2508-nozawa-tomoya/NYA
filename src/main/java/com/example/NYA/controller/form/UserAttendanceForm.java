package com.example.NYA.controller.form;

import com.example.NYA.repository.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class UserAttendanceForm {

    private Integer id;
    private User user;
    private Date workDate;
    private Time startTime;
    private Time endTime;
    private Time startRest;
    private Time endRest;
    private Integer status;
    private String comment;
    private Timestamp createDate;
    private Timestamp updatedDate;

    private boolean approved;

}
