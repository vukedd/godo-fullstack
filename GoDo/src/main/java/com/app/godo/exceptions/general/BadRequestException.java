package com.app.godo.exceptions.general;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}