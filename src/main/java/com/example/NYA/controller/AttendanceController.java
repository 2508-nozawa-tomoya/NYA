package com.example.NYA.controller;

import com.example.NYA.controller.form.AttendanceForm;
import com.example.NYA.service.AttendanceService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

import static com.example.NYA.error.ErrorMessages.E0020;

@Controller
public class AttendanceController {

    @Autowired
    AttendanceService attendanceService;

    // 勤怠編集画面表示
    @GetMapping("/attendance/edit/{id}")
    public ModelAndView editAttendance(@PathVariable String id,
                                       RedirectAttributes attributes) {

        List<String> errorMessages = new ArrayList<>();

        if (StringUtils.isBlank(id) || !id.matches("^[0-9]+$")) {
            errorMessages.add(E0020);
            attributes.addFlashAttribute("errorMessages", errorMessages);
            return new ModelAndView("redirect:/");
        }

        AttendanceForm attendance = attendanceService.selectAttendanceById(Integer.valueOf(id));

        if (attendance == null) {
            errorMessages.add(E0020);
            attributes.addFlashAttribute("errorMessages", errorMessages);
            return new ModelAndView("redirect:/");
        }

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/attendance/edit");
        mav.addObject("formModel", attendance);
        return mav;
    }


    // 勤怠編集処理
    @PutMapping("/attendance/update/{id}")
    public ModelAndView updateAttendance(@ModelAttribute("formModel")
                                         @Validated AttendanceForm attendanceForm,
                                         BindingResult result) {

        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView();
            mav.addObject("formModel", attendanceForm);
            mav.setViewName("/attendance/edit");
            return mav;
        }

        attendanceService.saveAttendance(attendanceForm);
        return new ModelAndView("redirect:/");
    }

    // 勤怠削除処理
    @DeleteMapping("/attendance/delete/{id}")
    public ModelAndView deleteAttendance(@PathVariable Integer id) {
        attendanceService.deleteAttendance(id);
        return new ModelAndView("redirect:/");
    }

}
