package com.dogukankedersiz.appointment_backend.service;

import com.dogukankedersiz.appointment_backend.entities.User;

import java.util.Optional;

public interface IUserService {
    Optional<User> findByEmail(String email);
}