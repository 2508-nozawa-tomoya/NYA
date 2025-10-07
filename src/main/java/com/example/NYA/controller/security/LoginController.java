package com.example.NYA.controller.security;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(HttpSession session, Model model){

        List<String> errorMessages = (List<String>) session.getAttribute("erroeMessages");

        if(errorMessages != null && !errorMessages.isEmpty()){
            model.addAttribute("errorMessages", errorMessages);

            String account = (String) session.getAttribute("account");
            model.addAttribute("account", account);

            session.removeAttribute("errorMessage");
            session.removeAttribute("account");
        }
        return "login";
    }
}
