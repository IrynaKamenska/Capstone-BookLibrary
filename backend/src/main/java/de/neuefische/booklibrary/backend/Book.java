package de.neuefische.booklibrary.backend;

import lombok.With;
import org.hibernate.validator.constraints.ISBN;

import javax.validation.constraints.NotEmpty;


@With
public record Book(
        String id,
        @NotEmpty
        String title,
        @NotEmpty
        String author,
        @NotEmpty
        @ISBN
        String isbn,
        BookState bookState) {
}
