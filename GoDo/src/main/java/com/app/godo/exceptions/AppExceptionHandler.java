package com.app.godo.exceptions;

import com.app.godo.dtos.error.ErrorResponseDto;
import com.app.godo.exceptions.general.BadRequestException;
import com.app.godo.exceptions.general.ConflictException;
import com.app.godo.exceptions.general.NotFoundException;
import com.app.godo.exceptions.general.UnauthorizedException;
import com.app.godo.exceptions.refreshToken.ValidationException;
import com.app.godo.exceptions.registration.RegistrationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

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

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(ValidationException ex) {
        HttpStatus status;
        String errorMessage = "";
        switch (ex.getErrorType()) {
            case BAD_REQUEST:
                status = HttpStatus.BAD_REQUEST;
                errorMessage = "Invalid token!";
                break;
            default:
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                errorMessage = "Internal server error";
                break;
        }

        return new ResponseEntity<>(new ErrorResponseDto(errorMessage, ex.getErrorType().name()), status);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<NotFoundException> handleNotFoundException(NotFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        String errorMessage = ex.getMessage();

        return new ResponseEntity<>(new NotFoundException(errorMessage), status);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ConflictException> handleConflictException(ConflictException ex) {
        HttpStatus status = HttpStatus.CONFLICT;
        String errorMessage = ex.getMessage();

        return new ResponseEntity<>(new ConflictException(errorMessage), status);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<UnauthorizedException> handleUnauthorizedException(UnauthorizedException ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        String errorMessage = ex.getMessage();

        return new ResponseEntity<>(new UnauthorizedException(errorMessage), status);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BadRequestException> handleBadRequestException(BadRequestException ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        String errorMessage = ex.getMessage();

        return new ResponseEntity<>(new BadRequestException(errorMessage), status);
    }
}
