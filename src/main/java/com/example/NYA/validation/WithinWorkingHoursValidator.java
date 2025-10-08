package com.example.NYA.validation;

import com.example.NYA.repository.entity.Attendance;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Duration;
import java.time.LocalTime;

public class WithinWorkingHoursValidator implements ConstraintValidator<WithinWorkingHours, Attendance> {

    @Override
    public void initialize(WithinWorkingHours annotation) {
    }

    @Override
    public boolean isValid(Attendance attendance, ConstraintValidatorContext context) {
        // nullチェック
        if (attendance == null) return true;
        if (attendance.getStartTime() == null || attendance.getEndTime() == null) return true;

        // 休憩時間
        Duration rest = Duration.ZERO;
        if (attendance.getStartRest() != null && attendance.getEndRest() != null) {
            rest = Duration.between(attendance.getStartRest(), attendance.getEndRest());
        }

        // 勤務時間 = 終了 - 開始 - 休憩
        Duration work = Duration.between(attendance.getStartTime(), attendance.getEndTime()).minus(rest);

        // 8時間未満ならNG
        Duration minWork = Duration.ofHours(8);
        return !work.minus(minWork).isNegative(); // work >= 8時間ならtrue
    }
}