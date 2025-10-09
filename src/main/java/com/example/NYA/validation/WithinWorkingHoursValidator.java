package com.example.NYA.validation;

import com.example.NYA.controller.form.AttendanceForm;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Duration;
import java.time.LocalTime;

public class WithinWorkingHoursValidator implements ConstraintValidator<WithinWorkingHours, AttendanceForm> {

    private int minWorkHours;
    private int restHours;

    @Override
    public void initialize(WithinWorkingHours constraintAnnotation) {
        this.minWorkHours = constraintAnnotation.minWorkHours();
        this.restHours = constraintAnnotation.maxRestHours();
    }

    @Override
    public boolean isValid(AttendanceForm form, ConstraintValidatorContext context) {
        if (form == null) return true;

        LocalTime start = form.getStartTime();
        LocalTime end = form.getEndTime();
        LocalTime startRest = form.getStartRest();
        LocalTime endRest = form.getEndRest();

        if (start == null || end == null || startRest == null || endRest == null) return true;

        Duration rest = Duration.between(startRest, endRest);
        if (rest.isNegative()) return true;

        Duration work = Duration.between(start, end).minus(rest);
        if (work.isNegative()) return true;

        Duration minWork = Duration.ofHours(minWorkHours);
        Duration requiredRest = Duration.ofHours(restHours);

        // どちらか違反なら false
        return (work.compareTo(minWork) >= 0) && rest.equals(requiredRest);
    }
}