package com.example.NYA.controller.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalTime;

import static com.example.NYA.error.ErrorMessages.E0002;
import static com.example.NYA.error.ErrorMessages.E0009;

@Getter
@Setter
public class UserForm {

    private Integer id;

    private String account;

    @NotEmpty(message = E0002)
    @Pattern(regexp = "^(?:$|[\\x21-\\x7E]{6,20})$", message = E0009)
    private String password;

    private String name;

    private Integer departmentId;

    private Integer authority;

    private LocalTime workStart;

    private LocalTime workEnd;

    private LocalTime restStart;

    private LocalTime restEnd;

    private short isStopped;

    private Timestamp createdDate;

    private Timestamp updatedDate;

}
