package de.neuefische.booklibrary.backend.security;

import lombok.With;

@With
public record AppUser(
        String id,
        String username,
        String password,
        AppUserRole role
) {
}
