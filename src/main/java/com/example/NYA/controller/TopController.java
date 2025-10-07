package com.example.NYA.controller;

import com.example.NYA.security.LoginUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TopController {

    @GetMapping
    public ModelAndView top(@AuthenticationPrincipal LoginUserDetails loginUser){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        return mav;
    }
}
