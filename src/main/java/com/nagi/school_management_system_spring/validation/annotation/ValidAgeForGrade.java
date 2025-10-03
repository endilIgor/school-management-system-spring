package com.nagi.school_management_system_spring.validation.annotation;

import com.nagi.school_management_system_spring.validation.validator.AgeForGradeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AgeForGradeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAgeForGrade {
    String message() default "Student age does not match the grade level requirements";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
