package de.neuefische.booklibrary.backend;

import lombok.With;

import javax.validation.constraints.NotBlank;


@With
public record Book(
        String id,
        @NotBlank
        String title,
        @NotBlank
        String author,
        @NotBlank
        String isbn,
        BookState bookState) {
}
