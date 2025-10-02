package com.nagi.school_management_system_spring.exception;

public class StudentAlreadyEnrolledException extends RuntimeException {

    public StudentAlreadyEnrolledException(String message) {
        super(message);
    }

    public StudentAlreadyEnrolledException(String message, Throwable cause) {
        super(message, cause);
    }
}
