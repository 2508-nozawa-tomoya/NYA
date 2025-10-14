package com.example.NYA.controller.form;

import com.example.NYA.repository.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class UserAttendanceForm {

    private Integer id;
    private User user;
    private LocalDate workDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime startRest;
    private LocalTime endRest;
    private Integer status;
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
