package com.example.cloudStorage.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class LoginRequest {
    @NotEmpty(message = "username couldn't be empty")
    @Size(min=2, max=60, message = "username should be between 2-60 characters")
    String username;

    @NotEmpty(message = "password couldn't be empty")
    @Size(min=3, max=20, message = "a password should be between 3-20 characters")
    String password;
}
