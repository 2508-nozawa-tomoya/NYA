package com.example.NYA.service;

import com.example.NYA.controller.form.AttendanceForm;
import com.example.NYA.repository.AttendanceRepository;
import com.example.NYA.repository.entity.Attendance;
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

    // レコード取得(全件)

    // レコード取得(ID)
    public AttendanceForm selectAttendanceById(Integer id) {

        Attendance result = attendanceRepository.findById(id).orElse(null);
        AttendanceForm attendanceForm = new AttendanceForm();

        if (result == null) {
            return null;
        } else {
            attendanceForm.setId(result.getId());
            attendanceForm.setUserId(result.getUserId());
            attendanceForm.setWorkDate(result.getWorkDate());
            attendanceForm.setStartTime(result.getStartTime());
            attendanceForm.setEndTime(result.getEndTime());
            attendanceForm.setRest(result.getRest());
            attendanceForm.setStatus(result.getStatus());
            attendanceForm.setComment(result.getComment());
            attendanceForm.setCreateDate(result.getCreateDate());
            attendanceForm.setUpdatedDate(result.getUpdatedDate());
        }
        return attendanceForm;
    }

    // レコード追加・更新
    public void saveAttendance(AttendanceForm attendanceForm) {
        Attendance attendance = setAttendanceEntity(attendanceForm);
        attendanceRepository.save(attendance);
    }

    // レコード更新(一括)

    // レコード削除
    public void deleteAttendance(Integer id) {
        attendanceRepository.deleteById(id);
    }

    // DBから取得したデータをFormに設定
    private List<AttendanceForm> setAttendanceForm(List<Attendance> results) {

        List<AttendanceForm> attendances = new ArrayList<>();

        for (Attendance result : results) {
            AttendanceForm attendance = new AttendanceForm();
            attendance.setId(result.getId());
            attendance.setUserId(result.getUserId());
            attendance.setWorkDate(result.getWorkDate());
            attendance.setStartTime(result.getStartTime());
            attendance.setEndTime(result.getEndTime());
            attendance.setRest(result.getRest());
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

        if (reqAttendance.getId() != null) {
            attendance.setId(reqAttendance.getId());
            attendance.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
        }
        attendance.setUserId(reqAttendance.getUserId());
        attendance.setWorkDate(reqAttendance.getWorkDate());
        attendance.setStartTime(reqAttendance.getStartTime());
        attendance.setEndTime(reqAttendance.getEndTime());
        attendance.setRest(reqAttendance.getRest());
        attendance.setStatus(reqAttendance.getStatus());
        attendance.setComment(reqAttendance.getComment());
        return attendance;
    }

}
