package com.example.cloudStorage.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignupRequest {
    @NotBlank(message = "username is required")
    @Size(min=2, max=20, message = "username should be between 2-20 characters")
    String username;

    @NotEmpty(message = "password is required")
    @Size(min=3, max=20, message = "a password should be between 3-20 characters")
    String password;
}

