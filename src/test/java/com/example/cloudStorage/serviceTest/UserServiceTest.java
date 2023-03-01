package com.example.cloudStorage.serviceTest;

import com.example.cloudStorage.entities.ERole;
import com.example.cloudStorage.entities.User;
import com.example.cloudStorage.repositories.UserRepository;
import com.example.cloudStorage.service.impl.UserServiceImpl;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.security.Principal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;


    @Mock
    private User userStub;

    private String defaultUsername = "user";
    private String defaultPassword = "pass";
    private ERole defaultRole = ERole.valueOf("ROLE_USER");

    @BeforeEach
    void setUp() {
        openMocks(this);
    }


    @Test
    void willShowTrueIfSaveUser() {
        when(userRepository.save(any(User.class))).thenReturn(
                new User(defaultUsername, defaultPassword, defaultRole)
        );

        User savedUser = userRepository.save(userStub);
        Assert.assertNotNull(savedUser);
        verify(userRepository, times(1)).save(any(User.class));

    }

    @Test
     void testFindByUsernameShouldBeNotNull() {

        when(userRepository.findByLogin(defaultUsername))
                .thenReturn(Optional.of(userStub));
        User userEхpected = userService.findByUsername(defaultUsername);
        Assert.assertNotNull(userEхpected);

        verify(userRepository, times(1)).findByLogin(defaultUsername);
    }





}
