package de.neuefische.booklibrary.backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository appUserRepository;

    public AppUser findByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    public String save(AppUser newAppUser, PasswordEncoder passwordEncoder) {
        if (findByUsername(newAppUser.username()) != null) {
            throw new UserAlreadyExistsException("User with this name already exists");
        }

        AppUser appUser = newAppUser
                .withId(UUID.randomUUID().toString())
                .withPasswordBcrypt(passwordEncoder.encode(newAppUser.rawPassword()))
                .withRawPassword("")
                .withRole(newAppUser.role());
        appUserRepository.save(appUser);
        return "Created user: " + newAppUser.username();
    }

    public void deleteAppUser(String id, String username) {
//        appUserRepository.deleteById(id);
        AppUser userFromDatabase = findByUsername(username);
        if (userFromDatabase.id().equals(id)) {
            appUserRepository.deleteById(id);
        } else
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "AppUser with id: " + id + " must not delere another user");
    }
}
