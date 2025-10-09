package com.example.NYA.controller.form;

import com.example.NYA.validation.WithinWorkingHours;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import static com.example.NYA.error.ErrorMessages.*;

@Getter
@Setter
@WithinWorkingHours
public class AttendanceForm {

    private Integer id;

    private Integer userId;

    @NotNull(message = E0004)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate workDate;

    @NotNull(message = E0005)
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @NotNull(message = E0006)
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endTime;

    @NotNull(message = E0007)
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startRest;

    @NotNull(message = E0008)
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endRest;

    private Integer status;
    private String comment;

    private Timestamp createdDate;
    private Timestamp updatedDate;

}
