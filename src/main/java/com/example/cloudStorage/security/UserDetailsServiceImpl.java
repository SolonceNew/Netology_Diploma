package com.example.cloudStorage.security;

import com.example.cloudStorage.entities.User;
import com.example.cloudStorage.repositories.UserRepository;
import com.example.cloudStorage.security.jwt.JwtUser;
import com.example.cloudStorage.util.exceptions.UsernameNotFoundException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByLogin(username);
        if(user.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        log.info("IN loadUserByUsername - user {} is successfully loaded", username);
        return new JwtUser(user.get());
    }


}
