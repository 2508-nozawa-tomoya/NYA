package com.example.NYA.service.dto;

import com.example.NYA.repository.entity.Attendance;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class TotalDto {
    private Integer userId;
    private List<Attendance> attendanceList;
    private Duration totalWorkTime;
    private Duration totalRestTime;
    private Duration totalOverTime;
    private LocalDate monthStart;
    private LocalDate monthEnd;
    // 表示用フォーマット済み文字列
    private String totalWorkTimeFormatted;
    private String totalRestTimeFormatted;
    private String totalOverTimeFormatted;
}