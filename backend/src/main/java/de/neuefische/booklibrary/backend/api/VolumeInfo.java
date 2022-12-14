package de.neuefische.booklibrary.backend.api;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.With;

import java.util.List;

@With
public record VolumeInfo(
        String title,
        List<String> authors,
        List<Isbn> industryIdentifiers,
        @JsonProperty("imageLinks") ImageLink imageLink,
        String previewLink,
        List<String> categories,
        String printType,
        int pageCount

) {
}
