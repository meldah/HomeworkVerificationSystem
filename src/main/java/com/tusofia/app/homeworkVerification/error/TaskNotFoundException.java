package com.tusofia.app.homeworkVerification.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Task not found!")
public class TaskNotFoundException extends RuntimeException {

    private int statusCode;

    public TaskNotFoundException() {
        this.statusCode = 404;
    }

    public TaskNotFoundException(String message) {
        super(message);
        this.statusCode = 404;
    }

    public int getStatusCode() {
        return statusCode;
    }
}