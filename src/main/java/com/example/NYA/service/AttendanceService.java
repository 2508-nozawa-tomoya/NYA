package com.example.NYA.service;

import com.example.NYA.controller.form.AttendanceForm;
import com.example.NYA.controller.form.UserAttendanceForm;
import com.example.NYA.repository.AttendanceRepository;
import com.example.NYA.repository.entity.Attendance;
import com.example.NYA.repository.entity.User;
import com.example.NYA.service.dto.TotalDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class AttendanceService {

    @Autowired
    AttendanceRepository attendanceRepository;

    //特定ユーザーの「その月の勤怠データ一覧」を取得して、日付順に並べて返す。
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

    //1日ごとの「実際の労働時間（休憩差し引き後）」を計算して "8:00" のように文字列で返す。
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

                //フォーマット指定
                result.add(formatDuration(workDuration));

            } else {
                result.add("-");
            }
        }
        return result;
    }

    //複数の勤怠データをまとめて保存する。
    @Transactional
    public void saveAll(List<Attendance> attendances) {
        attendanceRepository.saveAll(attendances);
    }

    //数値のステータス（0,1,2,3）を日本語に変換して表示用にする。
    public String getStatusText(List<Attendance> list) {
        int status = list.stream()
                .filter(a -> a.getStatus() != null)
                .mapToInt(Attendance::getStatus)
                .findFirst()
                .orElse(0);

        return switch (status) {
            case 1 -> "申請中";
            case 2 -> "承認済み";
            case 3 -> "差し戻し";
            default -> "未申請";
        };
    }

    //「すでに申請中の勤怠があるか？」を判定する。
    public boolean isAlreadyApplied(List<Attendance> list) {
        return list.stream()
                .anyMatch(a -> a.getStatus() != null && (a.getStatus() == 1 || a.getStatus() == 2));
    }

    //1日ごとの「残業時間」を求める（8時間を超えた分だけ）。
    public List<String> getOvertimeHours(List<Attendance> attendances) {
        List<String> overList = new ArrayList<>();

        for (Attendance a : attendances) {
            Duration rest = Duration.ZERO;
            if (a.getStartRest() != null && a.getEndRest() != null) {
                rest = Duration.between(a.getStartRest(), a.getEndRest());
            }

            Duration work = Duration.between(a.getStartTime(), a.getEndTime()).minus(rest);

            // 超過時間（8時間を超えた分だけ）
            Duration base = Duration.ofHours(8);
            Duration over = Duration.ZERO;
            if (work.compareTo(base) > 0) {
                over = work.minus(base);
            }

            overList.add(formatDuration(over));
        }
        return overList;
    }

    //指定した月の「合計値」をまとめる（トータルの勤務時間・休憩時間・残業時間）
    public TotalDto getMonthly(Integer userId, LocalDate monthStart, LocalDate monthEnd) {

        // ① 期間内の勤怠データを取得
        List<Attendance> attendances =
                attendanceRepository.findByUserIdAndWorkDateBetween(userId, monthStart, monthEnd);

        // ② 合計値を初期化
        Duration totalWork = Duration.ZERO;
        Duration totalRest = Duration.ZERO;
        Duration totalOver = Duration.ZERO;

        // ③ 各日の労働時間を集計
        for (Attendance a : attendances) {
            Duration rest = Duration.ZERO;

            // 休憩時間 = endRest - startRest（両方がnullでなければ計算）
            if (a.getStartRest() != null && a.getEndRest() != null) {
                rest = Duration.between(a.getStartRest(), a.getEndRest());
            }

            // 労働時間 = (終了 - 開始) - 休憩
            Duration work = Duration.between(a.getStartTime(), a.getEndTime()).minus(rest);

            totalWork = totalWork.plus(work);
            totalRest = totalRest.plus(rest);

            // 超過時間（8時間超過分）
            Duration base = Duration.ofHours(8);
            if (work.compareTo(base) > 0) {
                totalOver = totalOver.plus(work.minus(base));
            }
        }

        // ④ DTOに詰めて返却
        TotalDto dto = new TotalDto();
        dto.setAttendanceList(attendances);
        dto.setTotalWorkTime(totalWork);
        dto.setTotalRestTime(totalRest);
        dto.setTotalOverTime(totalOver);
        dto.setMonthStart(monthStart);
        dto.setMonthEnd(monthEnd);
        dto.setTotalWorkTimeFormatted(formatDuration(totalWork));
        dto.setTotalRestTimeFormatted(formatDuration(totalRest));
        dto.setTotalOverTimeFormatted(formatDuration(totalOver));

        return dto;
    }

    //Duration（時間の差を表すオブジェクト）を "hh:mm" 形式の文字列に直す共通関数。
    private String formatDuration(Duration d) {
        long hours = d.toHours();
        long minutes = d.minusHours(hours).toMinutes();
        return String.format("%02d:%02d", hours, minutes);
    }

    // レコード取得(ID)
    public AttendanceForm selectAttendanceById(Integer id) {

        Attendance result = attendanceRepository.findById(id).orElse(null);
        AttendanceForm attendanceForm = new AttendanceForm();

        if (result == null) {
            return null;
        } else {
            attendanceForm.setId(result.getId());
            attendanceForm.setUserId(result.getUser().getId());
            attendanceForm.setWorkDate(result.getWorkDate());
            attendanceForm.setStartTime(result.getStartTime());
            attendanceForm.setEndTime(result.getEndTime());
            attendanceForm.setStartRest(result.getStartRest());
            attendanceForm.setEndRest(result.getEndRest());
            attendanceForm.setStatus(result.getStatus());
            attendanceForm.setCreatedDate(result.getCreatedDate());
            attendanceForm.setUpdatedDate(result.getUpdatedDate());
        }
        return attendanceForm;
    }

    // レコード取得(ステータス)
    public List<UserAttendanceForm> selectAttendanceByStatus(Integer departmentId, int status) {
        List<Attendance> results = attendanceRepository.findAttendanceByStatus(departmentId, status);
        return setUserAttendanceForm(results);
    }

    // レコード追加・更新
    public void saveAttendance(AttendanceForm attendanceForm) {
        Attendance attendance = setAttendanceEntity(attendanceForm);
        attendanceRepository.save(attendance);
    }

    // レコード更新(一括承認)
    public void approve(List<Integer> approveIds) {
        for (Integer approveId : approveIds) {
            Attendance attendance = attendanceRepository.findById(approveId).orElseThrow();
            attendance.setStatus(2);
            attendanceRepository.save(attendance);
        }
    }

    // レコード更新(一括差戻し)
    public void reject(List<Integer> approveIds) {

        for (Integer approveId : approveIds) {
            Attendance attendance = attendanceRepository.findById(approveId).orElseThrow();
            attendance.setStatus(3);
            attendanceRepository.save(attendance);
        }
    }

    // レコード削除
    public void deleteAttendance(Integer id) {
        attendanceRepository.deleteById(id);
    }

    // DBから取得したデータをFormに設定
    private List<UserAttendanceForm> setUserAttendanceForm(List<Attendance> results) {

        List<UserAttendanceForm> attendances = new ArrayList<>();

        for (Attendance result : results) {
            UserAttendanceForm attendance = new UserAttendanceForm();
            attendance.setId(result.getId());
            attendance.setUser(result.getUser());
            attendance.setWorkDate(result.getWorkDate());
            attendance.setStartTime(result.getStartTime());
            attendance.setEndTime(result.getEndTime());
            attendance.setStartRest(result.getStartRest());
            attendance.setEndRest(result.getEndRest());
            attendance.setStatus(result.getStatus());
            attendance.setCreatedDate(result.getCreatedDate());
            attendance.setUpdatedDate(result.getUpdatedDate());
            attendances.add(attendance);
        }
        return attendances;
    }

    // リクエストから取得した情報をentityに設定
    private Attendance setAttendanceEntity(AttendanceForm reqAttendance) {

        Attendance attendance = new Attendance();
        User user = new User();
        user.setId(reqAttendance.getUserId());

        if (reqAttendance.getId() != null) {
            attendance.setId(reqAttendance.getId());
            attendance.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
        }
        attendance.setUser(user);
        attendance.setWorkDate(reqAttendance.getWorkDate());
        attendance.setStartTime(reqAttendance.getStartTime());
        attendance.setEndTime(reqAttendance.getEndTime());
        attendance.setStartRest(reqAttendance.getStartRest());
        attendance.setEndRest(reqAttendance.getEndRest());
        attendance.setStatus(reqAttendance.getStatus());
        return attendance;
    }
}