package com.digitalid.exception;

// Thrown when an organisation tries to do an action it is not authorised to do.

public class AuthorizationException extends RuntimeException {

    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
