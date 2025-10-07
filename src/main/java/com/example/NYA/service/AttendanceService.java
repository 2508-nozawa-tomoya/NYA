package com.example.NYA.service;

import com.example.NYA.repository.AttendanceRepository;
import com.example.NYA.repository.entity.Attendance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class AttendanceService {
    @Autowired
    AttendanceRepository attendanceRepository;

    public List<Attendance> getMonthlyAttendance(Integer userId, LocalDate monthStart, LocalDate monthEnd, LocalTime startTime, LocalTime endTime) {
        // ① 1か月分を取得
        List<Attendance> list =
                attendanceRepository.findByUserIdAndWorkDateBetween(userId, monthStart, monthEnd);

        // ② nullチェック
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }

        // ③ null対応＆ラムダで使うためfinal化
        final LocalTime filterStart = (startTime != null) ? startTime : LocalTime.of(0, 0);
        final LocalTime filterEnd   = (endTime != null) ? endTime   : LocalTime.of(23, 59);

        // ④ 時間帯でフィルタ
        List<Attendance> filtered = list.stream()
                .filter(a -> a.getStartTime() != null && a.getEndTime() != null)
                .filter(a -> !a.getStartTime().isBefore(filterStart)) // 開始が指定より前じゃない
                .filter(a -> !a.getEndTime().isAfter(filterEnd))       // 終了が指定より後じゃない
                .sorted(Comparator.comparing(Attendance::getWorkDate))
                .toList();

        return filtered;
    }

    public List<String> getWorkingHours(List<Attendance> attendances) {
        List<String> result = new ArrayList<>();

        for (Attendance a : attendances) {
            if (a.getStartTime() != null && a.getEndTime() != null && a.getRest() != null) {
                // 勤務時間
                Duration workDuration = Duration.between(a.getStartTime(), a.getEndTime());
                // LocalTime rest を Duration に変換
                Duration restDuration = Duration.ofHours(a.getRest().getHour())
                        .plusMinutes(a.getRest().getMinute());
                // 実労働時間 = 全体 - 休憩
                workDuration = workDuration.minus(restDuration);
                System.out.println("check " + a.getStartTime() + "~" + a.getEndTime());
                long h = workDuration.toHours();
                long m = workDuration.toMinutesPart();
                result.add(String.format("%d:%02d", h, m));
            } else {
                result.add("-");
            }
        }
        return result;
    }
}
