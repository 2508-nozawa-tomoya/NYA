package com.example.NYA.controller;

import com.example.NYA.security.LoginUserDetails;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TopController {

    @GetMapping("/")
    public ModelAndView top(@AuthenticationPrincipal LoginUserDetails loginUser, HttpSession session){
        ModelAndView mav = new ModelAndView();

        //CustomAuthenticationFailureHandlerで出たエラーを拾う
        String errorMessage = (String) session.getAttribute("errorMessage");
        if (errorMessage != null) {
            mav.addObject("errorMessage", errorMessage);
            session.removeAttribute("errorMessage");
        }

        mav.setViewName("index");
        mav.addObject("userId", loginUser.getId());
        return mav;
    }
}
