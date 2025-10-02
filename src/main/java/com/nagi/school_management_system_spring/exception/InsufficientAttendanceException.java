package com.nagi.school_management_system_spring.exception;

public class InsufficientAttendanceException extends RuntimeException {

    public InsufficientAttendanceException(String message) {
        super(message);
    }

    public InsufficientAttendanceException(String message, Throwable cause) {
        super(message, cause);
    }
}
