package de.neuefische.booklibrary.backend.security;

import de.neuefische.booklibrary.backend.SecurityConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository appUserRepository;

    public AppUser findByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    public AppUser save(AppUser newAppUser) {
        if (findByUsername(newAppUser.username()) != null) {
            throw new UserAlreadyExistsException("User with name " + newAppUser.username() + " already exists");
        }
        String encodedPassword = SecurityConfig.passwordEncoder.encode(newAppUser.password());
        AppUser appUser = newAppUser
                .withId(UUID.randomUUID().toString())
                .withPassword(encodedPassword)
                .withRole(newAppUser.role());
        return appUserRepository.save(appUser);
    }

    public boolean isAppUserExists(String id) {
        return appUserRepository.existsById(id);
    }

    public void deleteAppUser(String id) {
        appUserRepository.deleteById(id);
    }
}
