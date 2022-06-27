package com.tusofia.app.homeworkVerification.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Course not found!")
public class CourseNotFoundException extends RuntimeException {

    private int statusCode;

    public CourseNotFoundException() {
        this.statusCode = 404;
    }

    public CourseNotFoundException(String message) {
        super(message);
        this.statusCode = 404;
    }

    public int getStatusCode() {
        return statusCode;
    }
}