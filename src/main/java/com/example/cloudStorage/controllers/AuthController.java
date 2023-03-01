package com.example.cloudStorage.controllers;

import com.example.cloudStorage.dto.request.LoginRequest;
import com.example.cloudStorage.dto.request.SignupRequest;
import com.example.cloudStorage.dto.response.JwtResponse;
import com.example.cloudStorage.dto.response.MessageResponse;
import com.example.cloudStorage.entities.JwtBlackListEntity;
import com.example.cloudStorage.entities.User;
import com.example.cloudStorage.security.jwt.JwtUtils;
import com.example.cloudStorage.service.impl.JwtBlackListServiceImpl;
import com.example.cloudStorage.service.impl.UserServiceImpl;

import com.example.cloudStorage.util.mapper.UserMapper;
import com.example.cloudStorage.util.validator.ResponseErrorValidator;
import com.example.cloudStorage.util.validator.UserValidator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


@AllArgsConstructor
@RestController
@RequestMapping("/")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("permitAll()")
@CrossOrigin
public class AuthController {

    AuthenticationManager authenticationManager;
    JwtUtils jwtUtils;
    UserServiceImpl userService;
    ResponseErrorValidator responseErrorValidator;
    UserMapper userMapper;
    UserValidator userValidator;
    JwtBlackListServiceImpl blackListService;



     @PostMapping("/login")
     ResponseEntity<?> login(@RequestBody LoginRequest authenticationDto) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
    new UsernamePasswordAuthenticationToken(authenticationDto.getLogin(),
            authenticationDto.getPassword());
        try {
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Credentials", e);


        }
        String token = jwtUtils.generateToken(authenticationDto.getLogin());
        Map<String, String> response = new HashMap<>();
        response.put("auth-token", token);
        return ResponseEntity.ok(new JwtResponse(true, token));
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response,
                                         @RequestHeader("auth-token") String authToken) {
        String jwt = authToken.substring(7);
        JwtBlackListEntity blackListToken = blackListService.saveInBlackList(jwt);
        if(blackListToken == null) return new ResponseEntity<>("Something went wrong",
                HttpStatus.INTERNAL_SERVER_ERROR);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return new ResponseEntity<>("Success logout", HttpStatus.OK);

    }


    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody @Valid SignupRequest signupRequest,
                                          BindingResult bindingResult) {
        User user = userMapper.convertToSignupUser(signupRequest);
        userValidator.validate(user, bindingResult);
        ResponseEntity<Object> errors = responseErrorValidator.mapValidationService(bindingResult);
        if(!ObjectUtils.isEmpty(errors)) return errors;
        userService.registration(user);

       return ResponseEntity.ok(new MessageResponse("User registered successfully"));



    }
}
