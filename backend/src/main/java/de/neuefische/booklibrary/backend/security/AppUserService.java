package de.neuefische.booklibrary.backend.security;

import de.neuefische.booklibrary.backend.SecurityConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository appUserRepository;

    public AppUser findByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    public AppUser save(AppUser appUser) {
        if (findByUsername(appUser.username()) != null) {
            throw new UserAlreadyExistsException("User with this name already exists");
        }
        String encodedPassword = SecurityConfig.passwordEncoder.encode(appUser.password());
        AppUser encodedAppUser = appUser.withPassword(encodedPassword);
        return appUserRepository.save(encodedAppUser);
    }
}
