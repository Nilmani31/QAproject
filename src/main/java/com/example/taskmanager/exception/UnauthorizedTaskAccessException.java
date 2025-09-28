package com.example.taskmanager.exception;

public class UnauthorizedTaskAccessException extends RuntimeException {
    public UnauthorizedTaskAccessException() {
        super("You cannot delete this task");
    }

}
