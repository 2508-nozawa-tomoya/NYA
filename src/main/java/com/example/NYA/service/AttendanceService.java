package com.example.NYA.service;

import com.example.NYA.controller.form.AttendanceForm;
import com.example.NYA.controller.form.UserAttendanceForm;
import com.example.NYA.repository.AttendanceRepository;
import com.example.NYA.repository.entity.Attendance;
import com.example.NYA.repository.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AttendanceService {

    @Autowired
    AttendanceRepository attendanceRepository;

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
            attendanceForm.setComment(result.getComment());
            attendanceForm.setCreateDate(result.getCreateDate());
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
    public void approve(List<UserAttendanceForm> selected) {
        for (UserAttendanceForm userAttendanceForm : selected) {
            Attendance attendance = attendanceRepository.findById(userAttendanceForm.getId()).orElseThrow();
            attendance.setStatus(3);
            attendance.setComment(userAttendanceForm.getComment());
            attendanceRepository.save(attendance);
        }
    }

    // レコード更新(一括差戻し)
    public void reject(List<UserAttendanceForm> selected) {
        for (UserAttendanceForm userAttendanceForm : selected) {
            Attendance attendance = attendanceRepository.findById(userAttendanceForm.getId()).orElseThrow();
            attendance.setStatus(4);
            attendance.setComment(userAttendanceForm.getComment());
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
            attendance.setComment(result.getComment());
            attendance.setCreateDate(result.getCreateDate());
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
        attendance.setComment(reqAttendance.getComment());
        return attendance;
    }

}
