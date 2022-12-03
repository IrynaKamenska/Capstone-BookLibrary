package de.neuefische.booklibrary.backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;

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
    public AppUser registerMember(@RequestBody AppUser newAppUser) {
        AppUser appUser = newAppUser.withRole(AppUserRole.MEMBER);
        try {
            return appUserService.save(appUser);
        } catch (UserAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }

    }

    @PostMapping("/librarian")
    @ResponseStatus(code = HttpStatus.CREATED)
    public AppUser registerLibrarian(@RequestBody AppUser newAppUser) {
        AppUser appUser = newAppUser.withRole(AppUserRole.LIBRARIAN);
        try {
            return appUserService.save(appUser);
        } catch (UserAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }

    }

    @GetMapping("/logout")
    public void logout(HttpSession httpSession) {
        httpSession.invalidate();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String id) {
        if (appUserService.isAppUserExists(id)) {
            appUserService.deleteAppUser(id);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No AppUser with id: " + id + " found");

    }


}
