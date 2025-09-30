package com.team48.gestiondestock.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class BadCredentialsException extends RuntimeException {

    private ErrorCodes errorCode;
    private List<String> errors;

    public BadCredentialsException(String message) {
        super(message);
    }

    public BadCredentialsException(String message, ErrorCodes errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BadCredentialsException(String message, Throwable cause, ErrorCodes errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public BadCredentialsException(String message, ErrorCodes errorCode, List<String> errors) {
        super(message);
        this.errorCode = errorCode;
        this.errors = errors;
    }

}
