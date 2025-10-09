package com.example.NYA.controller.form;

import com.example.NYA.validation.WithinWorkingHours;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@WithinWorkingHours
public class AttendanceForm {
    private Integer id;
    private Integer userId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "日付を入力してください")
    private LocalDate workDate;

    @DateTimeFormat(pattern = "HH:mm")
    @NotNull(message = "開始時間を入力してください")
    private LocalTime startTime;

    @DateTimeFormat(pattern = "HH:mm")
    @NotNull(message = "終了時間を入力してください")
    private LocalTime endTime;

    @DateTimeFormat(pattern = "HH:mm")
    @NotNull(message = "休憩開始時間を入力してください")
    private LocalTime startRest;

    @DateTimeFormat(pattern = "HH:mm")
    @NotNull(message = "休憩終了時間を入力してください")
    private LocalTime endRest;

    private Short status;
    private String comment;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
