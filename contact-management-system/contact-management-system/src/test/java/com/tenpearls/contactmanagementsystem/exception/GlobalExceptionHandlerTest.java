package com.tenpearls.contactmanagementsystem.exception;

import jakarta.validation.ConstraintViolationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.core.MethodParameter;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @Mock
    private MethodParameter methodParameter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        handler = new GlobalExceptionHandler();
    }

    @Test
    void shouldHandleContactNotFound() {

        ContactNotFoundException ex =
                new ContactNotFoundException("Contact not found");

        ResponseEntity<Map<String, Object>> response =
                handler.handleContactNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(404, response.getBody().get("status"));
        assertEquals("Contact not found", response.getBody().get("message"));
    }

    @Test
    void shouldHandleUserNotFound() {

        UserNotFoundException ex =
                new UserNotFoundException("User not found");

        ResponseEntity<Map<String, Object>> response =
                handler.handleUserNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(404, response.getBody().get("status"));
        assertEquals("User not found", response.getBody().get("message"));
    }

    @Test
    void shouldHandleEmailAlreadyExists() {

        EmailAlreadyExistsException ex =
                new EmailAlreadyExistsException("Email already exists");

        ResponseEntity<Map<String, Object>> response =
                handler.handleEmailExists(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(409, response.getBody().get("status"));
        assertEquals("Email already exists", response.getBody().get("message"));
    }

    @Test
    void shouldHandlePhoneAlreadyExists() {

        PhoneNumberAlreadyExistsException ex =
                new PhoneNumberAlreadyExistsException("Phone number already exists");

        ResponseEntity<Map<String, Object>> response =
                handler.handlePhoneExists(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(409, response.getBody().get("status"));
        assertEquals("Phone number already exists", response.getBody().get("message"));
    }

    @Test
    void shouldHandleInvalidCredentials() {

        InvalidCredentialsException ex =
                new InvalidCredentialsException("Invalid credentials");

        ResponseEntity<Map<String, Object>> response =
                handler.handleInvalidCredentials(ex);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(401, response.getBody().get("status"));
        assertEquals("Invalid credentials", response.getBody().get("message"));
    }

    @Test
    void shouldHandleInvalidPassword() {

        InvalidPasswordException ex =
                new InvalidPasswordException("Invalid password");

        ResponseEntity<Map<String, Object>> response =
                handler.handleInvalidPassword(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().get("status"));
        assertEquals("Invalid password", response.getBody().get("message"));
    }

    @Test
    void shouldHandleDataIntegrityViolation() {

        DataIntegrityViolationException ex =
                new DataIntegrityViolationException("Duplicate value");

        ResponseEntity<Map<String, Object>> response =
                handler.handleDataIntegrityViolation(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(409, response.getBody().get("status"));
        assertEquals(
                "Email or phone number already exists",
                response.getBody().get("message")
        );
    }

    @Test
    void shouldHandleConstraintViolation() {

        ConstraintViolationException ex =
                new ConstraintViolationException(
                        "Constraint violation",
                        Collections.emptySet()
                );

        ResponseEntity<Map<String, Object>> response =
                handler.handleConstraint(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().get("status"));
        assertEquals("Constraint violation", response.getBody().get("message"));
    }

    @Test
    void shouldHandleMalformedRequest() {

        HttpInputMessage inputMessage = null;

        HttpMessageNotReadableException ex =
                new HttpMessageNotReadableException(
                        "Malformed JSON",
                        inputMessage
                );

        ResponseEntity<Map<String, Object>> response =
                handler.handleMessageNotReadable(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().get("status"));
        assertEquals("Invalid request body", response.getBody().get("message"));
    }

    @Test
    void shouldHandleGeneralException() {

        Exception ex =
                new Exception("Unexpected error");

        ResponseEntity<Map<String, Object>> response =
                handler.handleGeneral(ex);

        assertEquals(
                HttpStatus.INTERNAL_SERVER_ERROR,
                response.getStatusCode()
        );

        assertEquals(500, response.getBody().get("status"));
        assertEquals(
                "An unexpected error occurred",
                response.getBody().get("message")
        );
    }

    @Test
    void shouldHandleValidationWithFieldError() {

        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(
                        new Object(),
                        "request"
                );

        bindingResult.addError(
                new FieldError(
                        "request",
                        "email",
                        "Invalid email address"
                )
        );

        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException(
                        methodParameter,
                        bindingResult
                );

        ResponseEntity<Map<String, Object>> response =
                handler.handleValidation(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().get("status"));
        assertEquals(
                "Invalid email address",
                response.getBody().get("message")
        );
    }

    @Test
    void shouldHandleValidationWithoutFieldError() {

        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(
                        new Object(),
                        "request"
                );

        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException(
                        methodParameter,
                        bindingResult
                );

        ResponseEntity<Map<String, Object>> response =
                handler.handleValidation(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().get("status"));
        assertEquals(
                "Validation failed",
                response.getBody().get("message")
        );
    }
}