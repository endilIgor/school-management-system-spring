package com.nagi.school_management_system_spring.exception;

public class TeacherNotQualifiedException extends RuntimeException {

    public TeacherNotQualifiedException(String message) {
        super(message);
    }

    public TeacherNotQualifiedException(String message, Throwable cause) {
        super(message, cause);
    }
}
