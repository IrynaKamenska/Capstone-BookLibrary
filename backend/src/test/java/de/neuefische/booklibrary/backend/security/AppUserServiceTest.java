package de.neuefische.booklibrary.backend.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

 class AppUserServiceTest {
     private final AppUserRepository mockAppUserRepository = mock(AppUserRepository.class);
     private final AppUserService appUserService = new AppUserService(mockAppUserRepository);
     private final BCryptPasswordEncoder mockPasswordEncoder = mock(BCryptPasswordEncoder.class);

     @Test
     void findByUsernameAndReturnUsername() {
         // given
         AppUser newAppUser = new AppUser("1", "Bob", "password", "", null);
         when(mockAppUserRepository.findByUsername(newAppUser.username())).thenReturn(Optional.of(newAppUser));

         // when
         AppUser actual = appUserService.findByUsername("Bob");

         // then
         verify(mockAppUserRepository).findByUsername(newAppUser.username());
         assertEquals(newAppUser, actual);
     }

     @Test
     void getAppUserInfoAndReturnAppUserInfo() {
         // given
         AppUser newAppUser = new AppUser("1", "Bob", "password", "", null);
         AppUserInfo appUserInfo = new AppUserInfo(newAppUser.username(), newAppUser.role());
         when(mockAppUserRepository.findByUsername(newAppUser.username())).thenReturn(Optional.of(newAppUser));

         // when
         AppUserInfo actual = appUserService.getUserInfo("Bob");

         // then
         verify(mockAppUserRepository).findByUsername(newAppUser.username());
         assertEquals(appUserInfo, actual);
     }

     @Test
     void addUserReturnsUserAlreadyExistsException() {
         //given
         String username = "testuser";
         AppUser newAppUser = new AppUser("1", username, "password", "", null);
         when(mockAppUserRepository.existsByUsername(username)).thenReturn(true);
         when(mockPasswordEncoder.encode("password")).thenReturn("encodedPassword");
         //when
         try {
             appUserService.save(newAppUser, mockPasswordEncoder);
             fail();
         } catch (UserAlreadyExistsException e) {
             //then
             verify(mockAppUserRepository).existsByUsername(username);
             verify(mockAppUserRepository, never()).save(newAppUser);
             assertEquals("User with this name already exists", e.getMessage());
         }
     }

     @Test
     void saveAppUserSuccessful() {
         //given
         AppUser newAppUser = new AppUser("id-1", "ira", "password", "", AppUserRole.MEMBER);

         newAppUser = newAppUser.withPasswordBcrypt("encodedPassword");
         AppUser encodedAppUser = newAppUser
                 .withId(newAppUser.id())
                 .withUsername(newAppUser.username())
                 .withRawPassword("")
                 .withPasswordBcrypt(newAppUser.passwordBcrypt())
                 .withRole(newAppUser.role());

         when(mockAppUserRepository.existsByUsername(newAppUser.username())).thenReturn(false);
         when(mockPasswordEncoder.encode(newAppUser.rawPassword())).thenReturn(newAppUser.passwordBcrypt());
         when(mockAppUserRepository.save(encodedAppUser)).thenReturn(encodedAppUser);

         //when
         AppUser actual = appUserService.save(newAppUser, mockPasswordEncoder);

         //then
         verify(mockPasswordEncoder).encode("password");
         verify(mockAppUserRepository).existsByUsername(newAppUser.username());
         verify(mockAppUserRepository).save(encodedAppUser);
         assertEquals(encodedAppUser, actual);

     }

    @Test
    void deleteAppUserSuccessful() {
        //given
        AppUser newAppUser = new AppUser("id-1", "username", "password", "", AppUserRole.MEMBER);
        String username = newAppUser.username();

        //when
        doNothing().when(mockAppUserRepository).deleteByUsername(username);
        appUserService.deleteAppUser(username);

        //then
        verify(mockAppUserRepository).deleteByUsername(username);
    }

     @Test
     void getAllUsernamesFromDb_returnAllUsernames() {
         //given
         String username1 = "username_1";
         String username2 = "username_2";
         List<String> usernames = new ArrayList<>(List.of(username1, username2));
         AppUser newAppUser1 = new AppUser("id-1", username1, "password", "", AppUserRole.MEMBER);
         AppUser newAppUser2 = new AppUser("id-2", username2, "password", "", AppUserRole.MEMBER);
         List<AppUser> users = new ArrayList<>(List.of(newAppUser1, newAppUser2));

         //when
         when(mockAppUserRepository.findAll()).thenReturn(users);
         List<String> actual = appUserService.getAllUsernamesFromDb();

         //then
         verify(mockAppUserRepository).findAll();
         assertEquals(usernames, actual);
     }

     @Test
     void getAllUsernamesFromDb_returnEmptyList() {
         //given
         List<String> emptyList = new ArrayList<>(List.of());
         List<AppUser> users = new ArrayList<>(List.of());

         //when
         when(mockAppUserRepository.findAll()).thenReturn(users);
         List<String> actual = appUserService.getAllUsernamesFromDb();

         //then
         verify(mockAppUserRepository).findAll();
         assertEquals(emptyList, actual);
     }

     @Test
     void whenAppUserExistsInDb_returnTrue() {
         //given
         String username = "username";

         //when
         when(mockAppUserRepository.existsByUsername(username)).thenReturn(true);
         boolean actual = appUserService.existsByUsername(username);

         //then
         assertTrue(actual);
     }

     @Test
     void whenAppUserDoesNotExistsInDb_returnFalse() {
         //given
         String username = "username";

         //when
         when(mockAppUserRepository.existsByUsername(username)).thenReturn(false);
         boolean actual = appUserService.existsByUsername(username);

         //then
         assertFalse(actual);
     }

     @Test
     void getAllUsernamesFromDbAnd_returnListWithOneName() {
         // given
         AppUser newAppUser = new AppUser("1", "Bob", "password", "", null);
         List<AppUser> appUsers = new ArrayList<>(List.of(newAppUser));
         List<String> names = new ArrayList<>(List.of(newAppUser.username()));
         when(mockAppUserRepository.findAll()).thenReturn(appUsers);

         // when
         List<String> actual = appUserService.getAllUsernamesFromDb();

         // then
         verify(mockAppUserRepository).findAll();
         assertEquals(names, actual);
     }

     @Test
     void getAllUsernamesFromDbAnd_returnEmptyList() {
         // given
         List<AppUser> appUsers = new ArrayList<>(List.of());
         List<String> names = new ArrayList<>(List.of());
         when(mockAppUserRepository.findAll()).thenReturn(appUsers);

         // when
         List<String> actual = appUserService.getAllUsernamesFromDb();

         // then
         verify(mockAppUserRepository).findAll();
         assertEquals(names, actual);
     }

 }
