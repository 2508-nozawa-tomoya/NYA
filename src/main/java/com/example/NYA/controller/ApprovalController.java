package com.example.NYA.controller;

import com.example.NYA.controller.form.UserAttendanceForm;
import com.example.NYA.repository.entity.User;
import com.example.NYA.security.LoginUserDetails;
import com.example.NYA.service.AttendanceService;
import com.example.NYA.service.dto.TotalDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.example.NYA.error.ErrorMessages.E0021;

@Controller
public class ApprovalController {

    @Autowired
    AttendanceService attendanceService;

    // 承認者画面表示
    @GetMapping("/approval")
    public ModelAndView approval(@RequestParam(value = "year", required = false) Integer year,
                                 @RequestParam(value = "month", required = false) Integer month,
                                 @AuthenticationPrincipal LoginUserDetails loginUser) {

        List<UserAttendanceForm> approvalAttendances =
                attendanceService.selectAttendanceByStatus(loginUser.getDepartmentId(), 1);

        List<User> approvalUsers = new ArrayList<>();
        for (UserAttendanceForm approvalAttendance : approvalAttendances) {
            User user = approvalAttendance.getUser();
                if (!approvalUsers.contains(user)) {
                    approvalUsers.add(user);
                }
        }

        // --- 月関連処理 ---
        LocalDate today = LocalDate.now();

        //三項演算子
        int targetYear = (year != null) ? year : today.getYear();
        int targetMonth = (month != null) ? month : today.getMonthValue();
        LocalDate monthStart = LocalDate.of(targetYear, targetMonth - 1, 1);
        LocalDate monthEnd = monthStart.withDayOfMonth(monthStart.lengthOfMonth());

        int approvalMonth = targetMonth - 1;

        List<TotalDto> totalDtoList = new ArrayList<>();
        for (User approvalUser : approvalUsers) {
            Integer userId = approvalUser.getId();
            TotalDto totalDto = attendanceService.getMonthly(userId, monthStart, monthEnd);
            totalDtoList.add(totalDto);
        }

        ModelAndView mav = new ModelAndView();
        mav.setViewName("approval/approval");
        mav.addObject("loginUser", loginUser);
        mav.addObject("approvalUsers", approvalUsers);
        mav.addObject("approvalAttendances", approvalAttendances);
        mav.addObject("totalDtoList", totalDtoList);
        mav.addObject("year", targetYear);
        mav.addObject("month", approvalMonth);
        return mav;
    }

    // 承認・差戻し処理
    @PutMapping("/approval")
    public ModelAndView updateApproval(@RequestParam(name = "approvedIds", required = false) List<Integer> approvedIds,
                                       @RequestParam String action,
                                       RedirectAttributes attributes) {

        if (approvedIds == null || approvedIds.isEmpty()) {
            List<String> errorMessages = new ArrayList<>();
            errorMessages.add(E0021);
            attributes.addFlashAttribute("errorMessages", errorMessages);
            return new ModelAndView("redirect:/approval");
        }

        if ("approve".equals(action)) {
            attendanceService.approve(approvedIds);
        } else if ("reject".equals(action)) {
            attendanceService.reject(approvedIds);
        }

        return new ModelAndView("redirect:/approval");
    }

}
