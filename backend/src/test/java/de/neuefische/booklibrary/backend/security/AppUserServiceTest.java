package de.neuefische.booklibrary.backend.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
        AppUser newAppUser = new AppUser("1", "Bob", "password", "", null);
//        newAppUser = newAppUser.withUsername("Bob");
        when(mockAppUserRepository.save(newAppUser)).thenReturn(newAppUser);
        when(mockPasswordEncoder.encode("password")).thenReturn("encodedPassword");

        String actual = appUserService.save(newAppUser, mockPasswordEncoder);
        String expected = "Created user: " + newAppUser.username();

        verify(mockPasswordEncoder).encode("password");

        assertEquals(expected, actual);


    }


}
