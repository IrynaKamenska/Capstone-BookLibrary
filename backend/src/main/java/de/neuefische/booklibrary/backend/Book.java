package de.neuefische.booklibrary.backend;

import lombok.With;
import org.hibernate.validator.constraints.ISBN;

import javax.validation.constraints.NotBlank;


@With
public record Book(
        String id,
        @NotBlank
        String title,
        @NotBlank
        String author,
        @NotBlank
        @ISBN
        String isbn,
        BookState bookState) {
}
