package com.example.NYA.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String account;
    @Column
    private String password;
    @Column
    private String name;
    @Column(name = "department_id")
    private Integer departmentId;
    @Column
    private Integer authority;
    @Column(name = "work_start")
    private LocalTime workStart;
    @Column(name = "work_end")
    private LocalTime workEnd;
    @Column(name = "rest_start")
    private LocalTime restStart;
    @Column(name = "rest_end")
    private LocalTime restEnd;
    @Column(name = "is_stopped", nullable = false)
    private Integer isStopped;
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}
