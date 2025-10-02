package com.nagi.school_management_system_spring.exception;

public class QuarterAlreadyClosedException extends RuntimeException {

    public QuarterAlreadyClosedException(String message) {
        super(message);
    }

    public QuarterAlreadyClosedException(String message, Throwable cause) {
        super(message, cause);
    }
}
