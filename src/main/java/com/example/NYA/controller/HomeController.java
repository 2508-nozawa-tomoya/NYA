package com.example.NYA.controller;

import com.example.NYA.controller.form.FilterForm;
import com.example.NYA.repository.entity.Attendance;
import com.example.NYA.repository.entity.User;
import com.example.NYA.service.AttendanceService;
import com.example.NYA.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    UserService userService;
    @Autowired
    AttendanceService attendanceService;

    @GetMapping("/")
    public ModelAndView showHome(@RequestParam(value="year", required=false) Integer year,
                                 @RequestParam(value="month", required=false) Integer month,
                                 @ModelAttribute("filterForm") FilterForm filterForm,
                                 HttpSession session) {
        ModelAndView mav = new ModelAndView("home");
        LocalTime startTime = filterForm.getStartTime();
        LocalTime endTime = filterForm.getEndTime();
        // 時間指定がない場合の初期値
        if (startTime == null) startTime = LocalTime.of(0, 0);
        if (endTime == null) endTime = LocalTime.of(23, 59);

        Integer userId = userService.getLoginUserId();
        // ユーザー情報も取得
        User loginUser = userService.getLoginUserById(userId);

        //一カ月毎に表示させる
        LocalDate today = LocalDate.now();
        int targetYear = (year != null) ? year : today.getYear();
        int targetMonth = (month != null) ? month : today.getMonthValue();
        LocalDate monthStart = LocalDate.of(targetYear, targetMonth, 1);
        LocalDate monthEnd = monthStart.withDayOfMonth(monthStart.lengthOfMonth());

        // 前月・翌月の年月を計算
        LocalDate prevMonthDate = monthStart.minusMonths(1);
        LocalDate nextMonthDate = monthStart.plusMonths(1);

        List<Attendance> attendanceList = attendanceService.getMonthlyAttendance(userId, monthStart, monthEnd, startTime, endTime);
        List<String> workingHoursList = attendanceService.getWorkingHours(attendanceList);

        System.out.println("startTime=" + startTime + ", endTime=" + endTime);
        mav.addObject("loginUser", loginUser);
        mav.addObject("attendances", attendanceList);
        mav.addObject("workingHour", workingHoursList);
        mav.addObject("filterForm", filterForm);
        mav.addObject("month", targetMonth);
        mav.addObject("year", targetYear);
        mav.addObject("prevYear", prevMonthDate.getYear());
        mav.addObject("prevMonth", prevMonthDate.getMonthValue());
        mav.addObject("nextYear", nextMonthDate.getYear());
        mav.addObject("nextMonth", nextMonthDate.getMonthValue());
        return mav;
    }
}
