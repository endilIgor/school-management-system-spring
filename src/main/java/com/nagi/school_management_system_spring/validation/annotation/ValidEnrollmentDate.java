package com.nagi.school_management_system_spring.validation.annotation;

import com.nagi.school_management_system_spring.validation.validator.EnrollmentDateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EnrollmentDateValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEnrollmentDate {
    String message() default "Enrollment date must be within valid school year dates";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
