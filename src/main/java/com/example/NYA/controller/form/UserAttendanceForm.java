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
    private Timestamp createdDate;
    private Timestamp updatedDate;

    private boolean approved;

    public String getStatusLabel() {
        if (status == null) return "";
        return switch (status) {
            case 0 -> "未申請";
            case 1 -> "申請中";
            case 2 -> "承認済";
            case 3 -> "差戻し";
            default -> "";
        };
    }

}
