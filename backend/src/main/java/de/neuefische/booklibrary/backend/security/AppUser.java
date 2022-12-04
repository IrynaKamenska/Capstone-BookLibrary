package de.neuefische.booklibrary.backend.security;

import lombok.With;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@With
public record AppUser(
        String id,
        @NotBlank(message = "User's name cannot be empty.")
        String username,
        @NotBlank(message = "User's password cannot be empty.")
        @Pattern(regexp = "^(?=[^A-Z]*+[A-Z])(?=[^a-z]*+[a-z])(?=\\D*+\\d)(?=[^#?!@$ %^&*-]*+[#?!@$ %^&*-]).{8,}$", message = "Password must have minimum eight characters, at least one letter and one number!")
        String rawPassword,
        String passwordBcrypt,
        AppUserRole role
) {
}
