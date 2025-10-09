package com.example.NYA.controller;

import com.example.NYA.controller.form.UserForm;
import com.example.NYA.service.UserService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

import static com.example.NYA.error.ErrorMessages.E0010;
import static com.example.NYA.error.ErrorMessages.E0019;

@Controller
public class PasswordSettingController {

    @Autowired
    UserService userService;

    // パスワード変更画面表示
    @GetMapping("/password/change/{id}")
    public ModelAndView changePassword(@PathVariable String id,
                                       RedirectAttributes attributes) {

        List<String> errorMessages = new ArrayList<>();
        if (StringUtils.isBlank(id) || !id.matches("^[0-9]+$")) {
            errorMessages.add(E0019);
            attributes.addFlashAttribute("errorMessages", errorMessages);
            return new ModelAndView("redirect:/");
        }

        UserForm user = userService.findById(Integer.valueOf(id));
        if (user == null) {
            errorMessages.add(E0019);
            attributes.addFlashAttribute("errorMessages", errorMessages);
            return new ModelAndView("redirect:/");
        }

        ModelAndView mav = new ModelAndView();
        mav.setViewName("change-password");
        mav.addObject("formModel", user);
        return mav;
    }

    // パスワード変更処理
    @PutMapping("password/update/{id}")
    public ModelAndView updatePassword(String confirmationPassword,
                                       @ModelAttribute("formModel") @Validated UserForm userForm,
                                       BindingResult result) {

        if (userForm.getPassword() != null) {
            String password = userForm.getPassword();
            if (!password.matches(confirmationPassword)) {
                FieldError fieldError = new FieldError(result.getObjectName(),
                        "password", E0010);
                result.addError(fieldError);
            }
        }

        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView();
            mav.addObject("formModel", userForm);
            mav.setViewName("/password-change");
            return mav;
        }

        userService.saveUser(userForm);
        return new ModelAndView("redirect:/");
    }

}
