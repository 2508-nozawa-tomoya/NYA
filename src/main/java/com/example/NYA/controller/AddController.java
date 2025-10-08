package com.example.NYA.controller;

import com.example.NYA.service.AttendanceService;
import com.example.NYA.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AddController {
    @Autowired
    UserService userService;
    @Autowired
    AttendanceService attendanceService;

    @PostMapping("/add")
    public ModelAndView addAttendance(HttpSession session,)
}
