package de.neuefische.booklibrary.backend.security;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AppUserRepository extends MongoRepository<AppUser, String> {
    Optional<AppUser> findByUsername(String username);

    boolean existsByUsername(String username);

    void deleteByUsername(String username);
}
