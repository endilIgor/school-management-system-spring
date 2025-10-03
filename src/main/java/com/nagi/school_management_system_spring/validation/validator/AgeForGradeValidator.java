package com.nagi.school_management_system_spring.validation.validator;

import com.nagi.school_management_system_spring.model.StudentModel;
import com.nagi.school_management_system_spring.validation.annotation.ValidAgeForGrade;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class AgeForGradeValidator implements ConstraintValidator<ValidAgeForGrade, StudentModel> {

    @Override
    public void initialize(ValidAgeForGrade constraintAnnotation) {
    }

    @Override
    public boolean isValid(StudentModel student, ConstraintValidatorContext context) {
        if (student == null || student.getUser() == null || student.getUser().getBirthDate() == null || student.getClassroom() == null) {
            return true;
        }

        LocalDate birthDate = student.getUser().getBirthDate();
        String grade = student.getClassroom().getGrade();

        int age = Period.between(birthDate, LocalDate.now()).getYears();

        return isAgeValidForGrade(age, grade);
    }

    private boolean isAgeValidForGrade(int age, String grade) {
        if (grade == null || grade.isEmpty()) {
            return true;
        }

        String gradeUpper = grade.toUpperCase();

        if (gradeUpper.contains("PRE") || gradeUpper.contains("INFANTIL")) {
            return age >= 3 && age <= 5;
        }

        if (gradeUpper.matches(".*1(ST)?.*ANO.*") || gradeUpper.contains("1º")) {
            return age >= 6 && age <= 8;
        }
        if (gradeUpper.matches(".*2(ND)?.*ANO.*") || gradeUpper.contains("2º")) {
            return age >= 7 && age <= 9;
        }
        if (gradeUpper.matches(".*3(RD)?.*ANO.*") || gradeUpper.contains("3º")) {
            return age >= 8 && age <= 10;
        }
        if (gradeUpper.matches(".*4(TH)?.*ANO.*") || gradeUpper.contains("4º")) {
            return age >= 9 && age <= 11;
        }
        if (gradeUpper.matches(".*5(TH)?.*ANO.*") || gradeUpper.contains("5º")) {
            return age >= 10 && age <= 12;
        }
        if (gradeUpper.matches(".*6(TH)?.*ANO.*") || gradeUpper.contains("6º")) {
            return age >= 11 && age <= 13;
        }
        if (gradeUpper.matches(".*7(TH)?.*ANO.*") || gradeUpper.contains("7º")) {
            return age >= 12 && age <= 14;
        }
        if (gradeUpper.matches(".*8(TH)?.*ANO.*") || gradeUpper.contains("8º")) {
            return age >= 13 && age <= 15;
        }
        if (gradeUpper.matches(".*9(TH)?.*ANO.*") || gradeUpper.contains("9º")) {
            return age >= 14 && age <= 16;
        }

        if (gradeUpper.contains("1") && gradeUpper.contains("SERIE")) {
            return age >= 15 && age <= 17;
        }
        if (gradeUpper.contains("2") && gradeUpper.contains("SERIE")) {
            return age >= 16 && age <= 18;
        }
        if (gradeUpper.contains("3") && gradeUpper.contains("SERIE")) {
            return age >= 17 && age <= 19;
        }

        return true;
    }
}
