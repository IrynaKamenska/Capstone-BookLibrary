package de.neuefische.booklibrary.backend.api;


import lombok.With;

import java.util.List;

@With
public record VolumeInfo(
        String title,
        List<String> authors,
        List<Isbn> industryIdentifiers,
        ImageLink imageLink,
        String previewLink

) {
}
