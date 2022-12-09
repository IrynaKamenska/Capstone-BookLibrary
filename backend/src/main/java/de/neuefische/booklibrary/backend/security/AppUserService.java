package de.neuefische.booklibrary.backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository appUserRepository;

    public AppUser findByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    public AppUserInfo getUserInfo(String username) {
        AppUser appUser = appUserRepository.findByUsername(username);
        return new AppUserInfo(appUser.username(), appUser.role());
    }

    public boolean existsByUsername(String username) {
        return appUserRepository.existsByUsername(username);
    }

    public List<String> getUsernamesFromDb() {
        return appUserRepository.findAll().stream().map(AppUser::username).toList();
    }

    public AppUser save(AppUser newAppUser, PasswordEncoder passwordEncoder) {
        if (appUserRepository.existsByUsername(newAppUser.username())) {
            throw new UserAlreadyExistsException("User with this name already exists");
        }

        AppUser appUser = newAppUser
                .withUsername(newAppUser.username())
                .withRawPassword("")
                .withPasswordBcrypt(passwordEncoder.encode(newAppUser.rawPassword()))
                .withRole(newAppUser.role());
        return appUserRepository.save(appUser);
    }

    public void deleteAppUser(String username) {
        appUserRepository.deleteByUsername(username);
    }
}
