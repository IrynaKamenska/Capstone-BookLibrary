package de.neuefische.booklibrary.backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository appUserRepository;

    public AppUser findByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    public AppUser save(AppUser newAppUser, PasswordEncoder passwordEncoder) {
        if (findByUsername(newAppUser.username()) != null) {
            throw new UserAlreadyExistsException("User with this name already exists");
        }

        AppUser appUser = newAppUser
                .withUsername(newAppUser.username())
                .withPasswordBcrypt(passwordEncoder.encode(newAppUser.rawPassword()))
                .withRawPassword("")
                .withRole(newAppUser.role());
        return appUserRepository.save(appUser);
    }

    public void deleteAppUser(String id, String username) {
        AppUser userFromDatabase = findByUsername(username);
        if (userFromDatabase.id().equals(id)) {
            appUserRepository.deleteById(id);
        } else
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "AppUser " + userFromDatabase.username() + " must not delete another user");
    }
}
