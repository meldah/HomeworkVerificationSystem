package com.tusofia.app.homeworkVerification.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Submission not found!")
public class SubmissionNotFoundException extends RuntimeException {

    private int statusCode;

    public SubmissionNotFoundException() {
        this.statusCode = 404;
    }

    public SubmissionNotFoundException(String message) {
        super(message);
        this.statusCode = 404;
    }

    public int getStatusCode() {
        return statusCode;
    }
}