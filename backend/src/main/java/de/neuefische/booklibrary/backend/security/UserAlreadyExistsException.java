package de.neuefische.booklibrary.backend.security;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String message) {
        super(message);

    }
}
