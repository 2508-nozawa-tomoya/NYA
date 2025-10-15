package com.example.NYA.repository;

import com.example.NYA.repository.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    List<Attendance> findByUserIdAndWorkDateBetween(Integer userId, LocalDate monthStart, LocalDate monthEnd);

    @Query("SELECT a " +
            "FROM Attendance a INNER JOIN a.user u " +
            "WHERE u.departmentId = :departmentId " +
            "AND a.status = :status " +
            "ORDER BY u.account ASC, a.workDate ASC")
    List<Attendance> findAttendanceByStatus(@Param("departmentId") Integer departmentId,
                                            @Param("status") int status);

}
