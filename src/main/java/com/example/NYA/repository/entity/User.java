package com.example.NYA.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.List;

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

    @Column
    private Integer departmentId;

    @Column
    private Integer authority;

    @Column
    private LocalTime workStart;

    @Column
    private LocalTime workEnd;

    @Column
    private LocalTime restStart;

    @Column
    private LocalTime restEnd;

    @Column
    private short isStopped;

    @Column(insertable = false, updatable = false)
    private Timestamp createdDate;

    @Column(insertable = false, updatable = true)
    private Timestamp updatedDate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Attendance> attendances;

}
