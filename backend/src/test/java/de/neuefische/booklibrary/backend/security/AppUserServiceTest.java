package de.neuefische.booklibrary.backend.security;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class AppUserServiceTest {
    private final AppUserRepository mockAppUserRepository = mock(AppUserRepository.class);
    private final AppUserService appUserService = new AppUserService(mockAppUserRepository);
    private final BCryptPasswordEncoder mockPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Test
    void findByUsernameAndReturnUsername() {
        // given
        AppUser newAppUser = new AppUser("1", "Bob", "password", "", null);
        when(mockAppUserRepository.findByUsername(newAppUser.username())).thenReturn(newAppUser);

        // when
        AppUser actual = appUserService.findByUsername("Bob");

        // then
        AppUser expected = newAppUser;
        assertEquals(expected, actual);
    }

    @Test
    void addUserReturnsUserAlreadyExistsException() {
        //given
        String username = "testuser";
        AppUser newAppUser = new AppUser("1", username, "password", "", null);
        when(mockAppUserRepository.findByUsername(username)).thenReturn(newAppUser);
        when(mockPasswordEncoder.encode("password")).thenReturn("encodedPassword");
        //when
        try {
            appUserService.save(newAppUser, mockPasswordEncoder);
            fail();
        } catch (UserAlreadyExistsException e) {
            //then
            assertEquals("User with this name already exists", e.getMessage());
        }
    }

    @Test
    void saveAppUserSuccessful() {
        //given
        AppUser newAppUser = new AppUser("id-1", "username", "password", "", AppUserRole.MEMBER);
        newAppUser = newAppUser.withPasswordBcrypt("encodedPassword");

        AppUser encodedAppUser = newAppUser
                .withId(newAppUser.id())
                .withUsername(newAppUser.username())
                .withRawPassword(newAppUser.rawPassword())
                .withPasswordBcrypt(newAppUser.passwordBcrypt())
                .withRole(newAppUser.role());

        when(mockPasswordEncoder.encode(newAppUser.rawPassword())).thenReturn(newAppUser.passwordBcrypt());
        when(mockAppUserRepository.save(newAppUser)).thenReturn(newAppUser);

        //when
        AppUser actual = appUserService.save(newAppUser, mockPasswordEncoder);
        AppUser expected = encodedAppUser;

        //then
        verify(mockPasswordEncoder).encode("password");

        assertEquals(expected, actual);

    }

    @Test
    void deleteAppUserSuccessful() {
        //given
        AppUser newAppUser = new AppUser("id-1", "username", "password", "", AppUserRole.MEMBER);
        newAppUser = newAppUser.withPasswordBcrypt("encodedPassword");
        when(mockAppUserRepository.findByUsername(newAppUser.username())).thenReturn(newAppUser);
        doNothing().when(mockAppUserRepository).deleteById("id-1");

        //when
        appUserService.deleteAppUser(newAppUser.id(), newAppUser.username());

        //then
        verify(mockAppUserRepository).deleteById(newAppUser.id());
    }

    @Test
    void deleteAppUser_return403() {
        //given
        String idToDelete = "1";
        AppUser userFromDatabase = new AppUser("2", "username", "password", "", AppUserRole.MEMBER);
        ResponseStatusException expectedResponseStatusException = new ResponseStatusException(HttpStatus.FORBIDDEN, "AppUser " + userFromDatabase.username() + " must not delete another user");
        when(mockAppUserRepository.findByUsername(userFromDatabase.username())).thenReturn(userFromDatabase);

        //when
        try {
            appUserService.deleteAppUser(idToDelete, userFromDatabase.username());
            //then
            fail();
        } catch (ResponseStatusException e) {
            assertEquals(expectedResponseStatusException.getMessage(), e.getMessage());
        }
    }

}
