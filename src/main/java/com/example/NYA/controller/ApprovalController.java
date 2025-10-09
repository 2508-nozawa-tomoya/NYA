package com.example.NYA.controller;

import com.example.NYA.controller.form.ApprovalForm;
import com.example.NYA.controller.form.UserAttendanceForm;
import com.example.NYA.repository.entity.User;
import com.example.NYA.security.LoginUserDetails;
import com.example.NYA.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.NYA.error.ErrorMessages.E0020;

@Controller
public class ApprovalController {

    @Autowired
    AttendanceService attendanceService;

    // 承認者画面表示
    @GetMapping("/approval")
    public ModelAndView approval(@AuthenticationPrincipal LoginUserDetails loginUser) {

        List<UserAttendanceForm> attendances =
                attendanceService.selectAttendanceByStatus(loginUser.getDepartmentId(), 1);

        Map<User, List<UserAttendanceForm>> groupedAttendances = new LinkedHashMap<>();
        for (UserAttendanceForm userAttendanceForm : attendances) {
            groupedAttendances
                    .computeIfAbsent(userAttendanceForm.getUser(), k -> new ArrayList<>())
                    .add(userAttendanceForm);
        }

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/approval/approval");
        mav.addObject("groupedAttendances", groupedAttendances);
        return mav;
    }

    // 承認・差戻し処理
    @PutMapping("/approval")
    public ModelAndView updateApproval(@ModelAttribute("formModel") @Validated ApprovalForm approvalForm,
                                       @RequestParam String action,
                                       RedirectAttributes attributes) {

        if (approvalForm.getUserAttendanceFormList() == null) {
            List<String> errorMessages = new ArrayList<>();
            errorMessages.add(E0020);
            attributes.addFlashAttribute("errorMessages", errorMessages);
            return new ModelAndView("redirect:/approval");
        }

        List<UserAttendanceForm> selected = approvalForm.getUserAttendanceFormList().stream()
                .filter(UserAttendanceForm::isApproved)
                .collect(Collectors.toList());

        if ("approve".equals(action)) {
            attendanceService.approve(selected);
        } else if ("reject".equals(action)) {
            attendanceService.reject(selected);
        }

        return new ModelAndView("redirect:/approval");
    }

}
