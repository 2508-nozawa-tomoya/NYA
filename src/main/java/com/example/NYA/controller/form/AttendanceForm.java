package com.example.NYA.controller.form;

import com.example.NYA.validation.WithinWorkingHours;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@WithinWorkingHours
public class AttendanceForm {
    private Integer id;
    private Integer userId;

    @NotNull(message = "日付を入力してください")
    private LocalDate workDate;

    @NotNull(message = "開始時間を入力してください")
    private LocalTime startTime;

    @NotNull(message = "終了時間を入力してください")
    private LocalTime endTime;

    @NotNull(message = "休憩開始時間を入力してください")
    private LocalTime startRest;

    @NotNull(message = "休憩終了時間を入力してください")
    private LocalTime endRest;

    private Short status;
    private String comment;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}