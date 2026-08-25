package com.example.bussinessSystem.entities;

import com.example.bussinessSystem.enums.Role_user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "Users")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory.")
    private String firstName;
    @NotBlank(message = "Name is mandatory.")
    private String lastName;

    @NotBlank(message = "Email is mandatory.")
    private String email;

//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Password is mandatory.")
    private String password;

    @NotBlank(message = "Phone is mandatory.")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role_user roleUser;

    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if(this.roleUser == null){
            this.roleUser = Role_user.CUSTOMER;
        }
    }
    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
