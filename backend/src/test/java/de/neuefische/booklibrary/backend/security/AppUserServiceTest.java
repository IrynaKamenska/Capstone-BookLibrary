package de.neuefische.booklibrary.backend.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

 class AppUserServiceTest {
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
         verify(mockAppUserRepository).findByUsername(newAppUser.username());
         assertEquals(newAppUser, actual);
     }

     @Test
     void getAppUserInfoAndReturnAppUserInfo() {
         // given
         AppUser newAppUser = new AppUser("1", "Bob", "password", "", null);
         AppUserInfo appUserInfo = new AppUserInfo(newAppUser.username(), newAppUser.role());
         when(mockAppUserRepository.findByUsername(newAppUser.username())).thenReturn(newAppUser);

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

}
