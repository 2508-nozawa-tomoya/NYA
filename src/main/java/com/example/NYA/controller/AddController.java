package com.example.NYA.controller;

import com.example.NYA.controller.form.AttendanceForm;
import com.example.NYA.controller.form.UserForm;
import com.example.NYA.security.LoginUserDetails;
import com.example.NYA.service.AttendanceService;
import com.example.NYA.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AddController {
    @Autowired
    UserService userService;
    @Autowired
    AttendanceService attendanceService;

    @PostMapping("/add")
    public ModelAndView addMessage(
            @ModelAttribute("messageForm") @Validated AttendanceForm attendanceForm,
            BindingResult bindingResult,
            @AuthenticationPrincipal LoginUserDetails loginUser,
            RedirectAttributes redirectAttributes) {

        // 入力内容に関するバリデーション
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.attendanceForm", bindingResult);
            redirectAttributes.addFlashAttribute("attendanceForm", attendanceForm);
            return new ModelAndView("redirect:/new");
        }

        // ログインユーザー情報を設定
        attendanceForm.setUserId(loginUser.getId());
        attendanceService.saveAttendance(attendanceForm);

        return new ModelAndView("redirect:/");
    }
}