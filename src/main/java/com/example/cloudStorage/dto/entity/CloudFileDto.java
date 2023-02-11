package com.example.cloudStorage.dto.entity;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;


@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Component
public class CloudFileDto {
    String fileName;
    long fileSize;





}
