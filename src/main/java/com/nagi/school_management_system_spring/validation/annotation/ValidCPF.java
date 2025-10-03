package com.nagi.school_management_system_spring.validation.annotation;

import com.nagi.school_management_system_spring.validation.validator.CPFValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CPFValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCPF {
    String message() default "Invalid CPF format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
