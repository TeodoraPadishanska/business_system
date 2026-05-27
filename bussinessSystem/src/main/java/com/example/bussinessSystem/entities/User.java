package com.example.bussinessSystem.entities;

import com.example.bussinessSystem.enums.Role_user;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
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
    private LocalDateTime createdAt ;

    private LocalDateTime lastLogin;

    public User(Long id, String firstName, String lastName, String email, String password, String phoneNumber, Role_user roleUser) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.roleUser = roleUser;
        this.createdAt = LocalDateTime.now();;
        this.lastLogin = LocalDateTime.now();;
    }

    public User() {
        this.createdAt = LocalDateTime.now();;
        this.lastLogin = LocalDateTime.now();;
    }
}
