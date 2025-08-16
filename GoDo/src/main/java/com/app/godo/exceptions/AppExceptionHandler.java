package com.app.godo.exceptions;

import com.app.godo.dtos.error.ErrorResponseDto;
import com.app.godo.exceptions.registration.RegistrationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<ErrorResponseDto> handleRegisterException(RegistrationException ex) {
        HttpStatus status;
        String errorMessage = switch (ex.getErrorType()) {
            case EMAIL_TAKEN -> {
                status = HttpStatus.CONFLICT;
                yield "Email is already in use";
            }
            case USERNAME_TAKEN -> {
                status = HttpStatus.CONFLICT;
                yield "Username is already in use";
            }
            default -> {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                yield "Internal server error";
            }
        };

        return new ResponseEntity<>(new ErrorResponseDto(errorMessage, ex.getErrorType().name()), status);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }
}
