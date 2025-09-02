package com.app.godo.exceptions.general;

import com.app.godo.exceptions.refreshToken.ValidationException;
import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
