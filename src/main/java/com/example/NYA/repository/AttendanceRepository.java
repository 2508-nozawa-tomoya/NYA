package com.example.NYA.repository;

import com.example.NYA.repository.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    List<Attendance> findByUserIdAndWorkDateBetween(Integer userId, LocalDate monthStart, LocalDate monthEnd);

}