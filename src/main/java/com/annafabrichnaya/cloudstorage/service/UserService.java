package com.annafabrichnaya.cloudstorage.service;

import com.annafabrichnaya.cloudstorage.entities.User;


public interface UserService {
    void registration(User user);
    User findByUsername(String username);

}
