package com.example.cloudStorage.controllers;


import com.example.cloudStorage.dto.entity.UserDto;
import com.example.cloudStorage.entities.User;
import com.example.cloudStorage.service.impl.UserServiceImpl;
import com.example.cloudStorage.util.mapper.UserMapper;
import com.example.cloudStorage.util.validator.ResponseErrorValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    UserServiceImpl userService;
    ResponseErrorValidator responseErrorValidator;
    UserMapper userMapper;


    @GetMapping("/")
    public ResponseEntity<UserDto> getCurrentUser(Principal principal) {
        User user = userService.getCurrentUser(principal);
        UserDto userDto = userMapper.convertToUserDto(user);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

}
