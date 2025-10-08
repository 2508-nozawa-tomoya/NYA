package com.example.NYA.controller;

import com.example.NYA.controller.form.UserForm;
import com.example.NYA.repository.entity.User;
import com.example.NYA.security.LoginUserDetails;
import com.example.NYA.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    UserService userService;

    /*
     * システム管理者画面表示
     */
    @GetMapping("/show")
    public ModelAndView show(@AuthenticationPrincipal LoginUserDetails loginUser){
        ModelAndView mav =  new ModelAndView();

        //ユーザー全件取得
        List<UserForm> users = userService.findAll();

        mav.setViewName("admin/index");
        mav.addObject("users", users);
        mav.addObject("loginUser", loginUser);
        return mav;
    }

    /*
     * 新規ユーザー登録画面表示
     */
    @GetMapping("/new")
    public ModelAndView newUser(Model model){
        ModelAndView mav = new ModelAndView();

        if(!model.containsAttribute("formModel")){
            //modelにformModelがない場合、空のFormをmavに保持させる(formModelが存在するとき=エラーでフォワード処理した時)
            UserForm userForm = new UserForm();
            mav.addObject("formModel", userForm);
        }

        mav.setViewName("admin/new");
        return mav;
    }

    /*
     * 新規ユーザー登録処理
     */
    @PostMapping("/add")
    public ModelAndView addUser(@ModelAttribute("formModel") @Validated UserForm userForm,
                                BindingResult result,
                                String confirmationPassword,
                                RedirectAttributes redirectAttributes){
        //ユーザーの重複チェック
        User user = userService.findByAccount(userForm.getAccount());
        if(user != null){
            FieldError error = new FieldError(result.getObjectName(),
                    "account",
                    "アカウントが重複しています");
            result.addError(error);
        }

        //パスワードと確認用パスワードの一致チェック
        if(!userForm.getPassword().equals(confirmationPassword)){
            FieldError error = new FieldError(result.getObjectName(), "password", "パスワードと確認用パスワードが一致しません");
            result.addError(error);
        }

        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.formModel", result);
            redirectAttributes.addFlashAttribute("formModel", userForm);
            return new ModelAndView("redirect:/admin/new");
        }

        userService.saveUser(userForm);
        return new ModelAndView("redirect:/admin/show");

    }

}
