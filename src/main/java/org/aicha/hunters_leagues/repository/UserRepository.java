package org.aicha.hunters_leagues.repository;

import org.aicha.hunters_leagues.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Page<User> findAll(Pageable pageable);

    Optional<User> findByEmail(String email);
    @Procedure(name = "DeleteUser")
    void deleteUser(@Param("id") UUID id);

    Optional<? extends User> findByUsername(String username);
}
