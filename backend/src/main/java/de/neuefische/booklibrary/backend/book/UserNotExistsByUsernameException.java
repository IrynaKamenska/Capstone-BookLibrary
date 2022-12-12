package de.neuefische.booklibrary.backend.book;

public class UserNotExistsByUsernameException extends RuntimeException {

    public UserNotExistsByUsernameException(String message) {
        super(message);

    }
}
