package de.neuefische.booklibrary.backend.api;

import de.neuefische.booklibrary.backend.BookState;
import lombok.With;

@With
public record ApiBook(
        String id,
        VolumeInfo volumeInfo,
        BookState bookState

) {

}
