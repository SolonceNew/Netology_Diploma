package com.example.cloudStorage.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@Validated
@Builder
@AllArgsConstructor
public class LoginRequest implements Serializable {
    @NotEmpty(message = "username couldn't be empty")
    @Size(min=2, max=60, message = "username should be between 2-60 characters")
    String login;

    @NotEmpty(message = "password couldn't be empty")
    @Size(min=3, max=20, message = "a password should be between 3-20 characters")
    String password;
}
