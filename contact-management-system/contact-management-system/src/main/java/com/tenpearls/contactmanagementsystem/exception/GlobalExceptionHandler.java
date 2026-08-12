package com.tenpearls.contactmanagementsystem.exception;
import org.springframework.dao.DataIntegrityViolationException;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.ZoneId;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Map<String, Object>> buildResponse(
            HttpStatus status,
            String message) {

        Map<String, Object> body = new LinkedHashMap<>();

        body.put("timestamp", LocalDateTime.now(ZoneId.systemDefault()));
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);

        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(ContactNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleContactNotFound(
            ContactNotFoundException ex) {

        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());

    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFound(
            UserNotFoundException ex) {

        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());

    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleEmailExists(
            EmailAlreadyExistsException ex) {

        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());

    }

@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<Map<String, Object>> handleValidation(
        MethodArgumentNotValidException ex) {

    String message = "Validation failed";

var fieldError = ex.getBindingResult().getFieldError();

if (fieldError != null) {
    message = fieldError.getDefaultMessage();
}

    return buildResponse(HttpStatus.BAD_REQUEST, message);
}

@ExceptionHandler(ConstraintViolationException.class)
public ResponseEntity<Map<String, Object>> handleConstraint(
        ConstraintViolationException ex) {

    return buildResponse(
            HttpStatus.BAD_REQUEST,
            ex.getMessage()
    );
}

@ExceptionHandler(DataIntegrityViolationException.class)
public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(
        DataIntegrityViolationException ex) {

    return buildResponse(
            HttpStatus.CONFLICT,
            "Email or phone number already exists"
    );
}

@ExceptionHandler(Exception.class)
public ResponseEntity<Map<String, Object>> handleGeneral(
        Exception ex) {

    return buildResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ex.getMessage()
    );
}

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidCredentials(
            InvalidCredentialsException ex) {

        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(PhoneNumberAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handlePhoneExists(
            PhoneNumberAlreadyExistsException ex) {

        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidPassword(
            InvalidPasswordException ex) {

        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

}