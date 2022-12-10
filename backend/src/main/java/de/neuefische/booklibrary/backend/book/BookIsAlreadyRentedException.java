package de.neuefische.booklibrary.backend.book;

public class BookIsAlreadyRentedException extends RuntimeException {
    public BookIsAlreadyRentedException(String message) {
        super(message);
    }
}
