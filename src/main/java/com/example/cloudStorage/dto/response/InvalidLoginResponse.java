package com.example.cloudStorage.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvalidLoginResponse {
    String username;
    String password;

    public InvalidLoginResponse() {
        this.username = "Invalid username";
        this.password = "Invalid password";
    }
}
