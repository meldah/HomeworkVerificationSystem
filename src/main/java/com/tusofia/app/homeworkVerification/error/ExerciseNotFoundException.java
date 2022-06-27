package com.tusofia.app.homeworkVerification.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Exercise not found!")
public class ExerciseNotFoundException extends RuntimeException {

    private int statusCode;

    public ExerciseNotFoundException() {
        this.statusCode = 404;
    }

    public ExerciseNotFoundException(String message) {
        super(message);
        this.statusCode = 404;
    }

    public int getStatusCode() {
        return statusCode;
    }
}