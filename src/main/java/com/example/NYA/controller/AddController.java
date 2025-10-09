package com.example.NYA.controller;

import com.example.NYA.controller.form.AttendanceForm;
import com.example.NYA.security.LoginUserDetails;
import com.example.NYA.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AddController {
    @Autowired
    AttendanceService attendanceService;

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("attendanceForm", new AttendanceForm());
        return "new";
    }

    @PostMapping("/add")
    public ModelAndView addMessage(
            @ModelAttribute("attendanceForm") @Validated AttendanceForm attendanceForm,
            BindingResult result,
            @AuthenticationPrincipal LoginUserDetails loginUser) {

        // 入力内容に関するバリデーション
        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView("new");
            mav.addObject("attendanceForm", attendanceForm);
            return mav;
        }

        // ログインユーザー情報を設定
        attendanceForm.setUserId(loginUser.getId());
        attendanceService.saveAttendance(attendanceForm);

        return new ModelAndView("redirect:/");
    }
}