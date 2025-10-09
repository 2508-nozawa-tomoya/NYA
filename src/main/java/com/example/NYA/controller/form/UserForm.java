package com.example.NYA.controller.form;

import com.example.NYA.validation.CreateGroup;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalTime;

import static com.example.NYA.error.ErrorMessages.*;

@Getter
@Setter
public class UserForm {

    private Integer id;

    @NotEmpty(message = E0001)
    @Pattern(regexp = "^(?:$|[0-9]{7})$", message = E0015)
    private String account;

    @NotEmpty(message = E0002, groups = {CreateGroup.class})
    @Pattern(regexp = "^(?:$|[\\x21-\\x7E]{6,20})$", message = E0010)
    private String password;

    @NotEmpty(message = E0012)
    @Pattern(regexp = "^(?:$|.*[^\\s　].*)$", message = E0012)
    @Size(min = 0, max = 10, message = E0016)
    private String name;

    @NotNull(message = E0013)
    private Integer departmentId;

    @NotNull(message = E0014)
    private Integer authority;

    @NotNull(message = E0005)
    private LocalTime workStart;

    @NotNull(message = E0006)
    private LocalTime workEnd;

    @NotNull(message = E0007)
    private LocalTime restStart;

    @NotNull(message = E0008)
    private LocalTime restEnd;

    private short isStopped;

    private Timestamp createdDate;

    private Timestamp updatedDate;

}
