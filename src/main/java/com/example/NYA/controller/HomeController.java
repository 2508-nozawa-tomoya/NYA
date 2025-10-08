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
    public ModelAndView showHome(
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "month", required = false) Integer month,
            HttpSession session) {

        ModelAndView mav = new ModelAndView("home");

        Integer userId = userService.getLoginUserId();
        User loginUser = userService.getLoginUserById(userId);

        // 一カ月毎に表示
        LocalDate today = LocalDate.now();
        int targetYear = (year != null) ? year : today.getYear();
        int targetMonth = (month != null) ? month : today.getMonthValue();
        LocalDate monthStart = LocalDate.of(targetYear, targetMonth, 1);
        LocalDate monthEnd = monthStart.withDayOfMonth(monthStart.lengthOfMonth());

        // 前月・翌月
        LocalDate prevMonthDate = monthStart.minusMonths(1);
        LocalDate nextMonthDate = monthStart.plusMonths(1);

        //勤怠データ取得
        List<Attendance> attendanceList =
                attendanceService.getMonthlyAttendance(userId, monthStart, monthEnd);

        List<String> workingHoursList = attendanceService.getWorkingHours(attendanceList);

        //申請は月に1回しか押せないようにする
        boolean alreadyApplied = attendanceList.stream()
                .anyMatch(a -> a.getStatus() != null && a.getStatus() == 1);

        mav.addObject("alreadyApplied", alreadyApplied);
        mav.addObject("loginUser", loginUser);
        mav.addObject("attendances", attendanceList);
        mav.addObject("workingHour", workingHoursList);
        mav.addObject("month", targetMonth);
        mav.addObject("year", targetYear);
        mav.addObject("prevYear", prevMonthDate.getYear());
        mav.addObject("prevMonth", prevMonthDate.getMonthValue());
        mav.addObject("nextYear", nextMonthDate.getYear());
        mav.addObject("nextMonth", nextMonthDate.getMonthValue());

        return mav;
    }
}
