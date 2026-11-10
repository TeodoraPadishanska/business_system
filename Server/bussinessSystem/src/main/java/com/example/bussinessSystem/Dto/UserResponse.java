package com.example.bussinessSystem.Dto;

import com.example.bussinessSystem.enums.Role_user;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private Role_user roleUser;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
}
