package com.moodvoice.api.repository;

import com.moodvoice.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // Пошук користувача за імейлом
}
