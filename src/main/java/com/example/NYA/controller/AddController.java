package com.example.NYA.controller;

import com.example.NYA.controller.form.AttendanceForm;
import com.example.NYA.controller.form.UserForm;
import com.example.NYA.security.LoginUserDetails;
import com.example.NYA.service.AttendanceService;
import com.example.NYA.service.UserService;
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

import java.time.Duration;
import java.time.LocalTime;

@Controller
public class AddController {
    @Autowired
    AttendanceService attendanceService;
    @Autowired
    UserService userService;

    @GetMapping("/new")
    public String showAddForm(@AuthenticationPrincipal LoginUserDetails loginUser,
                              Model model) {

        //所定労働時間の計算
        UserForm user = userService.findById(loginUser.getId());
        LocalTime workStart = user.getWorkStart();
        LocalTime workEnd = user.getWorkEnd();
        LocalTime restStart = user.getRestStart();
        LocalTime restEnd = user.getRestEnd();

        Duration workTime = Duration.between(workStart, workEnd);
        Duration restTime = Duration.between(restStart, restEnd);

        workTime = workTime.minus(restTime);
        String defaultWorkTime = attendanceService.formatDuration(workTime);

        model.addAttribute("attendanceForm", new AttendanceForm());
        model.addAttribute("loginUser", user);
        model.addAttribute("defaultWorkTime", defaultWorkTime);
        return "new";
    }

    @PostMapping("/add")
    public ModelAndView addMessage(
            @Validated @ModelAttribute("attendanceForm") AttendanceForm attendanceForm,
            BindingResult result,
            @AuthenticationPrincipal LoginUserDetails loginUser) {

        // 入力内容に関するバリデーション
        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView("new");

            //所定労働時間の計算
            UserForm user = userService.findById(loginUser.getId());
            LocalTime workStart = user.getWorkStart();
            LocalTime workEnd = user.getWorkEnd();
            LocalTime restStart = user.getRestStart();
            LocalTime restEnd = user.getRestEnd();

            Duration workTime = Duration.between(workStart, workEnd);
            Duration restTime = Duration.between(restStart, restEnd);

            workTime = workTime.minus(restTime);
            String defaultWorkTime = attendanceService.formatDuration(workTime);

            mav.addObject("attendanceForm", attendanceForm);
            mav.addObject("loginUser", user);
            mav.addObject("defaultWorkTime", defaultWorkTime);
            return mav;
        }

        // ログインユーザー情報を設定
        attendanceForm.setUserId(loginUser.getId());
        attendanceService.saveAttendance(attendanceForm);

        return new ModelAndView("redirect:/");
    }
}