package de.neuefische.booklibrary.backend.api;

import de.neuefische.booklibrary.backend.Availability;
import lombok.With;

@With
public record ApiBook(
        String id,
        VolumeInfo volumeInfo,
        Availability availability

) {

}
