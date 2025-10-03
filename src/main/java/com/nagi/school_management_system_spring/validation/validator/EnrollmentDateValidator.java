package com.nagi.school_management_system_spring.validation.validator;

import com.nagi.school_management_system_spring.validation.annotation.ValidEnrollmentDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class EnrollmentDateValidator implements ConstraintValidator<ValidEnrollmentDate, LocalDate> {

    @Override
    public void initialize(ValidEnrollmentDate constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDate enrollmentDate, ConstraintValidatorContext context) {
        if (enrollmentDate == null) {
            return true;
        }

        LocalDate now = LocalDate.now();

        if (enrollmentDate.isAfter(now)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Enrollment date cannot be in the future")
                   .addConstraintViolation();
            return false;
        }

        LocalDate minimumDate = LocalDate.of(1950, 1, 1);
        if (enrollmentDate.isBefore(minimumDate)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Enrollment date cannot be before 1950")
                   .addConstraintViolation();
            return false;
        }

        int enrollmentYear = enrollmentDate.getYear();
        int currentYear = now.getYear();
        int yearDifference = currentYear - enrollmentYear;

        if (yearDifference > 20) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Enrollment date is too old (more than 20 years)")
                   .addConstraintViolation();
            return false;
        }

        return true;
    }
}
