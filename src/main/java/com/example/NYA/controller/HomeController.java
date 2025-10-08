package com.example.NYA.controller;

import com.example.NYA.repository.entity.Attendance;
import com.example.NYA.security.LoginUserDetails;
import com.example.NYA.service.AttendanceService;
import com.example.NYA.service.UserService;
import com.example.NYA.service.dto.TotalDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
            @AuthenticationPrincipal LoginUserDetails loginUser) {

        ModelAndView mav = new ModelAndView("home");
        //ログイン中のユーザーの勤怠情報を把握するため
        Integer userId = userService.getLoginUserId();

        // --- 月関連処理 ---
        LocalDate today = LocalDate.now();

        //三項演算子
        int targetYear = (year != null) ? year : today.getYear();
        int targetMonth = (month != null) ? month : today.getMonthValue();
        LocalDate monthStart = LocalDate.of(targetYear, targetMonth, 1);
        LocalDate monthEnd = monthStart.withDayOfMonth(monthStart.lengthOfMonth());

        // --- 勤怠データ ---
        List<Attendance> attendanceList = attendanceService.getMonthlyAttendance(userId, monthStart, monthEnd);
        List<String> workingHoursList = attendanceService.getWorkingHours(attendanceList);
        List<String> overHourList = attendanceService.getOvertimeHours(attendanceList);
        TotalDto totalDto = attendanceService.getMonthly(userId, monthStart, monthEnd);

        // --- ステータス ---
        String statusText = attendanceService.getStatusText(attendanceList);
        boolean alreadyApplied = attendanceService.isAlreadyApplied(attendanceList);

        // --- 月ナビゲーション ---
        LocalDate prev = monthStart.minusMonths(1);
        LocalDate next = monthStart.plusMonths(1);

        // --- Viewにセット ---
        mav.addObject("loginUser", loginUser);
        mav.addObject("attendances", attendanceList);
        mav.addObject("workingHour", workingHoursList);
        mav.addObject("overHour", overHourList);
        mav.addObject("totalDto", totalDto);
        mav.addObject("status", statusText);
        mav.addObject("alreadyApplied", alreadyApplied);
        mav.addObject("month", targetMonth);
        mav.addObject("year", targetYear);
        mav.addObject("prevYear", prev.getYear());
        mav.addObject("prevMonth", prev.getMonthValue());
        mav.addObject("nextYear", next.getYear());
        mav.addObject("nextMonth", next.getMonthValue());

        return mav;
    }

    @PostMapping("/application")
    public String submitApplication(
            @RequestParam("year") int year,
            @RequestParam("month") int month) {

        Integer userId = userService.getLoginUserId();

        LocalDate monthStart = LocalDate.of(year, month, 1);
        LocalDate monthEnd = monthStart.withDayOfMonth(monthStart.lengthOfMonth());

        List<Attendance> attendanceList =
                attendanceService.getMonthlyAttendance(userId, monthStart, monthEnd);

        // すでに申請済みならスキップ
        boolean alreadyApplied = attendanceList.stream()
                .anyMatch(a -> a.getStatus() != null && a.getStatus() == 1);
        if (alreadyApplied) {
            return "redirect:/?year=" + year + "&month=" + month;
        }

        // 申請更新
        for (Attendance a : attendanceList) {
            a.setStatus(1);
            a.setUpdatedDate(LocalDateTime.now());
        }
        attendanceService.saveAll(attendanceList);

        //リダイレクト時も該当月を維持
        return "redirect:/?year=" + year + "&month=" + month;
    }
}
