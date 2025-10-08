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

    public List<Attendance> getMonthlyAttendance(Integer userId, LocalDate monthStart, LocalDate monthEnd) {
        // ① 1か月分を取得
        List<Attendance> list =
                attendanceRepository.findByUserIdAndWorkDateBetween(userId, monthStart, monthEnd);

        // ② nullチェック
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }

        // ③ 日付順にソートして返す
        return list.stream()
                .sorted(Comparator.comparing(Attendance::getWorkDate))
                .toList();
    }

    public List<String> getWorkingHours(List<Attendance> attendances) {
        List<String> result = new ArrayList<>();

        for (Attendance a : attendances) {
            if (a.getStartTime() != null && a.getEndTime() != null) {
                // 勤務時間
                Duration workDuration = Duration.between(a.getStartTime(), a.getEndTime());

                // 休憩時間を差し引く（両方nullでない場合）
                if (a.getStartRest() != null && a.getEndRest() != null) {
                    Duration restDuration = Duration.between(a.getStartRest(), a.getEndRest());
                    workDuration = workDuration.minus(restDuration);
                }

                // 結果フォーマット
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
