package com.example.bussinessSystem.entities;

import com.example.bussinessSystem.enums.Role_user;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Users")

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(unique = true)
    private String phoneNumber;

    //?
    @Enumerated(EnumType.STRING)
    private Role_user roleUser;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime lastLogin;


}
