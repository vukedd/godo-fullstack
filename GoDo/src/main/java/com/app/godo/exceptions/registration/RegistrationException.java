package com.app.godo.exceptions.registration;

import lombok.Getter;

@Getter
public class RegistrationException extends RuntimeException {
    private final RegistrationException.ErrorType errorType;

    public enum ErrorType {
        EMAIL_TAKEN,
        USERNAME_TAKEN
    }

    public RegistrationException(String message, RegistrationException.ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }
}
