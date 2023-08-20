package de.anevis.backend.exception;

public class DuplicateBookException extends RuntimeException {
    public DuplicateBookException(String message) {
        super(message);
    }
}
