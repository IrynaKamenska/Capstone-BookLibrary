package de.neuefische.booklibrary.backend;

import lombok.With;

@With
public record Book(String id, String title, String author, String isbn, BookState bookState) {
}
