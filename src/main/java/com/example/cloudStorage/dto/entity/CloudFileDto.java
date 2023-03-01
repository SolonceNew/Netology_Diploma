package com.example.cloudStorage.dto.entity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.io.Serializable;


@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Component
@AllArgsConstructor
public class CloudFileDto implements Serializable, Comparable<CloudFileDto> {
    @JsonProperty(value = "filename")
    String fileName;
    int size;

    @Override
    public int compareTo(CloudFileDto o) {
        return (this.getFileName().compareTo(o.getFileName()));
    }





}
