package com.example.cloudStorage.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username")
})
@Entity
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @NotBlank(message = "Username shouldn't be empty")
    @Size(min=2, max=20, message = "Username should be between 2 and 20 characters")
    @Column(unique = true, name = "username")
    String username;

    @NotBlank
    @Size(min=3, max=60, message = "Password should be between 3 and 60 characters")
    @Column(nullable = false)
    String password;


    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    ERole role;


    public User(String username, String password, ERole role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }


}
