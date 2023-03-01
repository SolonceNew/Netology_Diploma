package com.example.cloudStorage.service.impl;




import com.example.cloudStorage.entities.ERole;
import com.example.cloudStorage.entities.User;
import com.example.cloudStorage.repositories.UserRepository;
import com.example.cloudStorage.service.UserService;
import com.example.cloudStorage.util.exceptions.UsernameNotFoundException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;



    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registration(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(ERole.ROLE_USER);
        userRepository.save(user);

    }


    public User findByUsername(String username) {
       return userRepository.findByLogin(username)
                .orElse(null);
    }


    public User getCurrentUser(Principal principal) {
        return getUserByPrincipal(principal);
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username
                        + " not found"));
    }

}


