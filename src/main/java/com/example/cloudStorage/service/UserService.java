package com.example.cloudStorage.service;

import com.example.cloudStorage.entities.User;

import java.security.Principal;

public interface UserService {
    void registration(User user);
    User findByUsername(String username);
    User getCurrentUser(Principal principal);
}
