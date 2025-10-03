package com.nagi.school_management_system_spring.validation.validator;

import com.nagi.school_management_system_spring.validation.annotation.ValidEnrollmentNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnrollmentNumberValidator implements ConstraintValidator<ValidEnrollmentNumber, String> {

    private static final String ENROLLMENT_PATTERN = "^[0-9]{4}[0-9]{6}$";

    @Override
    public void initialize(ValidEnrollmentNumber constraintAnnotation) {
    }

    @Override
    public boolean isValid(String enrollmentNumber, ConstraintValidatorContext context) {
        if (enrollmentNumber == null || enrollmentNumber.isEmpty()) {
            return false;
        }

        enrollmentNumber = enrollmentNumber.replaceAll("[^0-9]", "");

        if (!enrollmentNumber.matches(ENROLLMENT_PATTERN)) {
            return false;
        }

        String yearPart = enrollmentNumber.substring(0, 4);
        int year = Integer.parseInt(yearPart);
        int currentYear = java.time.Year.now().getValue();

        if (year < 1900 || year > currentYear) {
            return false;
        }

        return true;
    }
}
