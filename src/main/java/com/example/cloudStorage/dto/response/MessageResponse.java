package com.example.cloudStorage.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageResponse {
    String message;

}
