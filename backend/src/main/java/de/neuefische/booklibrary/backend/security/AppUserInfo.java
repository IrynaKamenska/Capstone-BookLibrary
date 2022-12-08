package de.neuefische.booklibrary.backend.security;

import lombok.With;

import javax.validation.constraints.NotBlank;

@With
public record AppUserInfo(
        @NotBlank(message = "User's name cannot be empty.")
        String username,
        AppUserRole role
) {
}
