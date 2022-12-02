package de.neuefische.booklibrary.backend.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.With;

import java.util.List;

@With
public record BookResponseElement(
        int totalItems,
        @JsonProperty("items") List<ApiBook> apiBookItems
) {
}
