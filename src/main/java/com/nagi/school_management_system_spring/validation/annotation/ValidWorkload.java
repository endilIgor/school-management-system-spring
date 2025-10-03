package com.nagi.school_management_system_spring.validation.annotation;

import com.nagi.school_management_system_spring.validation.validator.WorkloadValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = WorkloadValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidWorkload {
    String message() default "Workload must be between 20 and 400 hours per year";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    int min() default 20;
    int max() default 400;
}
