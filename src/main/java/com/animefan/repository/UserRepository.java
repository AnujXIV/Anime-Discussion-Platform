package com.animefan.repository;

import com.animefan.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    // 🔐 Role-based support
    List<User> findAllByRole(String role);

    Optional<User> findByUsernameAndRole(String username, String role);
}
