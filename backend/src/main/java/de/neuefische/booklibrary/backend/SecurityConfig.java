package de.neuefische.booklibrary.backend;

import de.neuefische.booklibrary.backend.security.AppUser;
import de.neuefische.booklibrary.backend.security.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private static final String EXCEPTION_MSG = "You cannot use this custom UserDetailsManager for this action.";
    private static final String LIBRARIAN_ROLE = "LIBRARIAN";

    private final AppUserService appUserService;

    public static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Bean
    public PasswordEncoder encoder() {
        return passwordEncoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                .httpBasic()
                .and()
                .authorizeRequests()

                .antMatchers(HttpMethod.GET, "/api/app-users/login").permitAll()
                .antMatchers(HttpMethod.GET, "/api/app-users/me").permitAll()
                .antMatchers(HttpMethod.GET, "/api/app-users/user").permitAll()
                .antMatchers(HttpMethod.GET, "/api/app-users/role").authenticated()
                .antMatchers(HttpMethod.GET, "/api/app-users/logout").authenticated()
                .antMatchers(HttpMethod.POST, "/api/app-users/member").permitAll()
                .antMatchers(HttpMethod.POST, "/api/app-users/librarian").permitAll()
                .antMatchers(HttpMethod.DELETE, "/api/app-users/deleteMe").authenticated()


                .antMatchers(HttpMethod.GET, "/api/books").permitAll()
                .antMatchers(HttpMethod.GET, "/api/books/search/**").hasRole(LIBRARIAN_ROLE)
                .antMatchers(HttpMethod.GET, "/api/books/isbn/**").hasRole(LIBRARIAN_ROLE)
                .antMatchers(HttpMethod.POST, "/api/books").hasRole(LIBRARIAN_ROLE)
                .antMatchers(HttpMethod.PUT, "/api/books/**").hasRole(LIBRARIAN_ROLE)
                .antMatchers(HttpMethod.DELETE, "/api/books/**").hasRole(LIBRARIAN_ROLE)

                .anyRequest().permitAll()
                .and()
                .formLogin()
                .disable()
                .build();
    }

    @Bean
    public UserDetailsManager userDetailsManager() {
        return new UserDetailsManager() {

            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                AppUser appUserFromDB = appUserService.findByUsername(username);
                if (appUserFromDB == null) {
                    throw new UsernameNotFoundException("Username not found");
                }
                return User.builder()
                        .username(username)
                        .password(appUserFromDB.passwordBcrypt())
                        .roles(appUserFromDB.role().toString())
                        .build();
            }

            @Override
            public void createUser(UserDetails user) {
                throw new UnsupportedOperationException(EXCEPTION_MSG);
            }

            @Override
            public void updateUser(UserDetails user) {
                throw new UnsupportedOperationException(EXCEPTION_MSG);
            }

            @Override
            public void deleteUser(String username) {
                throw new UnsupportedOperationException(EXCEPTION_MSG);
            }

            @Override
            public void changePassword(String oldPassword, String newPassword) {
                throw new UnsupportedOperationException(EXCEPTION_MSG);
            }

            @Override
            public boolean userExists(String username) {
                throw new UnsupportedOperationException(EXCEPTION_MSG);
            }

        };
    }

}
