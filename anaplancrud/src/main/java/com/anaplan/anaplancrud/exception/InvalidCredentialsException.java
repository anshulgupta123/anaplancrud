package com.anaplan.anaplancrud.exception;

public class InvalidCredentialsException extends RuntimeException {
    private String message;

    public InvalidCredentialsException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
