package com.example.cloudStorage.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignupRequest implements Serializable {
    @NotEmpty(message = "username is required")
    @Size(min=2, max=20, message = "username should be between 2-20 characters")
    String login;

    @NotEmpty(message = "password is required")
    @Size(min=3, max=20, message = "a password should be between 3-20 characters")
    String password;
}

