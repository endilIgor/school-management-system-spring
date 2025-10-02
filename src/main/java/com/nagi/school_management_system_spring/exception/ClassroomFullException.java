package com.nagi.school_management_system_spring.exception;

public class ClassroomFullException extends RuntimeException {

    public ClassroomFullException(String message) {
        super(message);
    }

    public ClassroomFullException(String message, Throwable cause) {
        super(message, cause);
    }
}
