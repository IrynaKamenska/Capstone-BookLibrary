package de.neuefische.booklibrary.backend;

import lombok.With;

import javax.validation.constraints.NotBlank;


@With
public record Book(
        String id,
        String cover,
        @NotBlank
        String title,
        String author,
        @NotBlank
        String isbn,
        BookState bookState) {
}
