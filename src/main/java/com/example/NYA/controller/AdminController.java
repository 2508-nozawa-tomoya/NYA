package com.example.NYA.controller;

import com.example.NYA.controller.form.UserForm;
import com.example.NYA.repository.entity.User;
import com.example.NYA.security.LoginUserDetails;
import com.example.NYA.service.UserService;
import com.example.NYA.validation.CreateGroup;
import jakarta.validation.groups.Default;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import static com.example.NYA.error.ErrorMessages.*;

import java.util.ArrayList;
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
    public ModelAndView addUser(@ModelAttribute("formModel") @Validated({Default.class, CreateGroup.class}) UserForm userForm,
                                BindingResult result,
                                @RequestParam("confirmationPassword") String confirmationPassword,
                                RedirectAttributes redirectAttributes){
        //ユーザーの重複チェック
        User user = userService.findByAccount(userForm.getAccount());
        if(user != null){
            FieldError error = new FieldError(result.getObjectName(),
                    "account", userForm.getAccount(),
                    false,
                    null,
                    null,
                    E0017);
            result.addError(error);
        }

        //パスワードと確認用パスワードの一致チェック
        if(!userForm.getPassword().equals(confirmationPassword)){
            FieldError error = new FieldError(result.getObjectName(), "password", E0011);
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

    /*
     * ユーザー編集画面表示
     */

    @GetMapping("/edit/{id}")
    public ModelAndView userEdit(@PathVariable String id,
                                 RedirectAttributes redirectAttributes,
                                 @AuthenticationPrincipal LoginUserDetails loginUser,
                                 Model model){

        List<String> errorMessages = new ArrayList<>();

        //取得したユーザーIDをチェック
        if(id == null || id.isEmpty() || !id.matches("^[0-9]+$")){
            errorMessages.add(E0020);
            redirectAttributes.addFlashAttribute("errorMessages", errorMessages);
            return new ModelAndView("redirect:/admin/show");
        }

        //編集対象のユーザー情報を取得
        UserForm user = userService.findById(Integer.valueOf(id));

        if(user == null){
            errorMessages.add(E0020);
            redirectAttributes.addFlashAttribute("errorMessages", errorMessages);
            return new ModelAndView("redirect:/admin/show");
        }

        ModelAndView mav = new ModelAndView();

        if(!model.containsAttribute("formModel")){
            //modelにformModelがない場合、編集元のユーザー情報を表示(formModelが存在するとき=エラーでリダイレクト処理した時)
            mav.addObject("formModel", user);
        }

        mav.setViewName("admin/edit");
        mav.addObject("loginUser", loginUser);
        return mav;
    }

    /*
     * ユーザー編集機能
     */
    @PutMapping("/update/{id}")
    public ModelAndView userUpdate(@ModelAttribute("formModel") @Validated({Default.class}) UserForm userForm,
                                   BindingResult result,
                                   @PathVariable Integer id,
                                   @RequestParam("confirmationPassword") String confirmationPassword,
                                   RedirectAttributes redirectAttributes){
        //ユーザーの重複チェック
        User user = userService.findByAccount(userForm.getAccount());
        if((user != null) && (!user.getId().equals(id))){
            FieldError error = new FieldError(result.getObjectName(),
                    "account", userForm.getAccount(),
                    false,
                    null,
                    null,
                    E0017);
            result.addError(error);
        }

        //パスワードと確認用パスワードの一致チェック
        if((userForm.getPassword() != null) && (!userForm.getPassword().equals(confirmationPassword))){
            FieldError error = new FieldError(result.getObjectName(), "password", E0011);
            result.addError(error);
        }

        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.formModel", result);
            redirectAttributes.addFlashAttribute("formModel", userForm);
            return new ModelAndView("redirect:/admin/edit/" + id);
        }

        userForm.setId(id);
        userService.saveUser(userForm);

        return new ModelAndView("redirect:/admin/show");
    }

    /*
     * ユーザー停止/有効切り替え
     */
    @PutMapping("/change/{id}")
    public ModelAndView changeIsStopped(@PathVariable Integer id,
                                        @RequestParam("isStopped") short isStopped){
        userService.changeIsStopped(id, isStopped);

        return new ModelAndView("redirect:/admin/show");
    }

}
