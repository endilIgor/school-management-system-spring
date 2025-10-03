package com.nagi.school_management_system_spring.validation.validator;

import com.nagi.school_management_system_spring.model.EventModel;
import com.nagi.school_management_system_spring.validation.annotation.ValidEventDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class EventDateValidator implements ConstraintValidator<ValidEventDate, EventModel> {

    @Override
    public void initialize(ValidEventDate constraintAnnotation) {
    }

    @Override
    public boolean isValid(EventModel event, ConstraintValidatorContext context) {
        if (event == null || event.getStartDate() == null || event.getEndDate() == null) {
            return true;
        }

        LocalDate startDate = event.getStartDate();
        LocalDate endDate = event.getEndDate();

        if (endDate.isBefore(startDate)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("End date must be after or equal to start date")
                   .addConstraintViolation();
            return false;
        }

        LocalDate now = LocalDate.now();
        LocalDate maxFutureDate = now.plusYears(2);

        if (startDate.isAfter(maxFutureDate)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Start date cannot be more than 2 years in the future")
                   .addConstraintViolation();
            return false;
        }

        return true;
    }
}
