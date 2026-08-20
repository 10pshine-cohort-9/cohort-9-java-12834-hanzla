package com.tenpearls.contactmanagementsystem.exception;

public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException(String message) {
        super(message);
    }
}