package com.example.cloudStorage.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class JwtResponse {
    boolean success;
    String token;




}
