package com.annafabrichnaya.cloudstorage.security;

import com.annafabrichnaya.cloudstorage.entities.User;
import com.annafabrichnaya.cloudstorage.repositories.UserRepository;
import com.annafabrichnaya.cloudstorage.security.jwt.JwtUser;
import com.annafabrichnaya.cloudstorage.util.exceptions.UsernameNotFoundException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    UserRepository userRepository;

    @Autowired
    public UserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        log.info("IN loadUserByUsername - user {} is successfully loaded", username);
        return new JwtUser(user);}

}
