package com.nagi.school_management_system_spring.validation.validator;

import com.nagi.school_management_system_spring.validation.annotation.ValidWorkload;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class WorkloadValidator implements ConstraintValidator<ValidWorkload, Integer> {

    private int min;
    private int max;

    @Override
    public void initialize(ValidWorkload constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(Integer workload, ConstraintValidatorContext context) {
        if (workload == null) {
            return true;
        }

        if (workload < min || workload > max) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                String.format("Workload must be between %d and %d hours per year", min, max))
                   .addConstraintViolation();
            return false;
        }

        if (workload % 20 != 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Workload must be a multiple of 20 hours")
                   .addConstraintViolation();
            return false;
        }

        return true;
    }
}
