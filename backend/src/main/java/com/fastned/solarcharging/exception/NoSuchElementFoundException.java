package com.fastned.solarcharging.exception;

/**
 * Exception thrown when there is no DB record for the given filter parameters
 */
public class NoSuchElementFoundException extends RuntimeException {

    public NoSuchElementFoundException() {
        super();
    }

    public NoSuchElementFoundException(String message) {
        super(message);
    }

    public NoSuchElementFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
