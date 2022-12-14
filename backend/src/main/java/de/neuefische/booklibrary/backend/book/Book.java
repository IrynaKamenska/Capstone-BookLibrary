package de.neuefische.booklibrary.backend.book;

import de.neuefische.booklibrary.backend.api.Isbn;
import lombok.With;

import javax.validation.constraints.NotBlank;
import java.util.List;


@With
public record Book(
        String id,
        String cover,
        @NotBlank
        String title,
        String author,

        List<Isbn> isbn,
        Availability availability,
        RentBookInfo rentBookInfo) {
}
