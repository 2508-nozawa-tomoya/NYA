package com.example.NYA.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.example.NYA.error.ErrorMessages.E0009;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = WithinWorkingHoursValidator.class)
public @interface WithinWorkingHours {
    String message() default E0009;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    int minWorkHours() default 8;   // 8時間未満NG
    int maxRestHours() default 1;   // 1時間超NG
}
