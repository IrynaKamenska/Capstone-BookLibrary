package de.neuefische.booklibrary.backend.security;

import de.neuefische.booklibrary.backend.SecurityConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/app-users")
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService appUserService;

    @GetMapping("/login")
    public HttpStatus login() {
        return HttpStatus.OK;

    }

    @GetMapping("/me")
    public String me() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

    @GetMapping("/role")
    public String getRole() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()
                .toString();
    }

    @PostMapping("/member")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Object> registerMember(@Valid @RequestBody AppUser newAppUser) {
        AppUser appUser = newAppUser.withRole(AppUserRole.MEMBER);
        try {
            appUserService.save(appUser, SecurityConfig.passwordEncoder);
            return new ResponseEntity<>("User successfully registered!", HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }

    }

    @PostMapping("/librarian")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Object> registerLibrarian(@Valid @RequestBody AppUser newAppUser) {
        AppUser appUser = newAppUser.withRole(AppUserRole.LIBRARIAN);
        try {
            appUserService.save(appUser, SecurityConfig.passwordEncoder);
            return new ResponseEntity<>("User successfully registered!", HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }

    }

    @GetMapping("/logout")
    public void logout(HttpSession httpSession) {
        httpSession.invalidate();
    }

/*    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser userFromDatabase = appUserService.findByUsername(username);
        if (userFromDatabase.id().equals(id)) {
            appUserService.deleteAppUser(id);
        } else
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "AppUser with id: " + id + " must not delete another user");
    }*/

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        appUserService.deleteAppUser(id, username);

    }
}
