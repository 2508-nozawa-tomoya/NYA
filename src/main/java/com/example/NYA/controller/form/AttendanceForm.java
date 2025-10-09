package com.example.NYA.controller.form;

import com.example.NYA.validation.WithinWorkingHours;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import static com.example.NYA.error.ErrorMessages.*;

@Getter
@Setter
@WithinWorkingHours
public class AttendanceForm {

    private Integer id;

    private Integer userId;

    @NotNull(message = E0004)
    private LocalDate workDate;

    @NotNull(message = E0005)
    private LocalTime startTime;

    @NotNull(message = E0006)
    private LocalTime endTime;

    @NotNull(message = "休憩開始時間を入力してください")
    private LocalTime startRest;

    @NotNull(message = "休憩終了時間を入力してください")
    private LocalTime endRest;

    private Integer status;
    private String comment;

    private Timestamp createdDate;

    private Timestamp updatedDate;

}
