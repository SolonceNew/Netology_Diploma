package com.example.cloudStorage.serviceTest;

import com.example.cloudStorage.entities.ERole;
import com.example.cloudStorage.entities.User;
import com.example.cloudStorage.repositories.UserRepository;
import com.example.cloudStorage.security.UserDetailsServiceImpl;
import com.example.cloudStorage.util.exceptions.UsernameNotFoundException;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class UserDetailsServiceTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserRepository userRepository;

    String testAuthToken = "Bearer_testAuthToken";



    @BeforeEach
    void setUp() {
        openMocks(this);
    }
    @Test
    void loadUserByUsername() {

        when(userRepository.findByLogin(anyString())).thenReturn(Optional.of(new User("name", "pass", ERole.ROLE_USER)));
        userDetailsService.loadUserByUsername(anyString());

        verify(userRepository, times(1)).findByLogin(any());
    }

    @Test
    void loadUserByUsername_UsernameNotFoundException() {

        when(userRepository.findByLogin(anyString())).thenReturn(Optional.empty());

        Assert.assertThrows(UsernameNotFoundException.class,

                () -> {
                    userDetailsService.loadUserByUsername(anyString());
                }

        );
    }
}
