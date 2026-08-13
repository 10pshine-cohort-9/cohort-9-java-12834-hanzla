package com.tenpearls.contactmanagementsystem.exception;

import org.springframework.dao.DataIntegrityViolationException;

import jakarta.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.http.converter.HttpMessageNotReadableException;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);


    private ResponseEntity<Map<String, Object>> buildResponse(
            HttpStatus status,
            String message) {

        Map<String, Object> body = new LinkedHashMap<>();

        body.put(
                "timestamp",
                LocalDateTime.now(ZoneId.systemDefault())
        );

        body.put(
                "status",
                status.value()
        );

        body.put(
                "error",
                status.getReasonPhrase()
        );

        body.put(
                "message",
                message
        );

        return new ResponseEntity<>(body, status);
    }


    @ExceptionHandler(ContactNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleContactNotFound(
            ContactNotFoundException ex) {

        return buildResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFound(
            UserNotFoundException ex) {

        return buildResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
    }


    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleEmailExists(
            EmailAlreadyExistsException ex) {

        return buildResponse(
                HttpStatus.CONFLICT,
                ex.getMessage()
        );
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException ex) {

        String message = "Validation failed";

        var fieldError =
                ex.getBindingResult().getFieldError();

        if (fieldError != null) {

            message = fieldError.getDefaultMessage();
        }

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                message
        );
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
    public ResponseEntity<Map<String, Object>>
    handleDataIntegrityViolation(
            DataIntegrityViolationException ex) {

        return buildResponse(
                HttpStatus.CONFLICT,
                "Email or phone number already exists"
        );
    }


    /*
     * Handles malformed JSON or unreadable request bodies.
     *
     * Do not expose the internal parser exception to the client.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>>
    handleMessageNotReadable(
            HttpMessageNotReadableException ex) {

logger.warn("Malformed request body received");

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Invalid request body"
        );
    }


    /*
     * Handles unexpected server-side exceptions.
     *
     * The complete exception is logged server-side,
     * while the client receives only a safe message.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(
            Exception ex) {

        logger.error(
                "Unexpected server error",
                ex
        );

        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred"
        );
    }


    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, Object>>
    handleInvalidCredentials(
            InvalidCredentialsException ex) {

        return buildResponse(
                HttpStatus.UNAUTHORIZED,
                ex.getMessage()
        );
    }


    @ExceptionHandler(PhoneNumberAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handlePhoneExists(
            PhoneNumberAlreadyExistsException ex) {

        return buildResponse(
                HttpStatus.CONFLICT,
                ex.getMessage()
        );
    }


    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Map<String, Object>>
    handleInvalidPassword(
            InvalidPasswordException ex) {

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
    }
}