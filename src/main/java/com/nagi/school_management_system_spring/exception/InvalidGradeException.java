package com.nagi.school_management_system_spring.exception;

public class InvalidGradeException extends RuntimeException {

    public InvalidGradeException(String message) {
        super(message);
    }

    public InvalidGradeException(String message, Throwable cause) {
        super(message, cause);
    }
}
