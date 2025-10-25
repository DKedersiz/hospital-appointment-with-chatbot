package com.dogukankedersiz.appointment_backend.repository;

import com.dogukankedersiz.appointment_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // Bu metot Optional<User> döndürmeli
    User findByEmailAndPassword(String email, String password); // Mevcut metot
}