package com.example.bussinessSystem.Services;

import com.example.bussinessSystem.Dto.LoginResponse;
import com.example.bussinessSystem.Dto.LoginUser;
import com.example.bussinessSystem.Dto.UserResponse;
import com.example.bussinessSystem.Exception.InvalidCredentialsException;
import com.example.bussinessSystem.Exception.ResourceNotFoundException;
import com.example.bussinessSystem.Mappers.UserMapper;
import com.example.bussinessSystem.Repositories.UserRepository;
import com.example.bussinessSystem.entities.User;
import com.example.bussinessSystem.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    final UserRepository userRepository;
    final JwtUtil jwtUtil;
    final PasswordEncoder passwordEncoder;
    final UserMapper userMapper;

    public UserService(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public List<UserResponse> getAllUsers(){
        return userMapper.listUserToUserResponse(userRepository.findAll());
    }
    public UserResponse getUserById(Long id){
        return userMapper.userToUserResponse(userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with id: " + id + "not found")));
    }

    public UserResponse addUser(User user){
        if(userRepository.existsByEmail(user.getEmail())){
            throw new IllegalArgumentException("This email already exists.");
        }else if(userRepository.existsByPhoneNumber(user.getPhoneNumber())){
            throw new IllegalArgumentException("This phone number already exists.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return userMapper.userToUserResponse(user);
    }


    public LoginResponse loginUser(LoginUser loginUser){
        User user = userRepository.findByEmail(loginUser.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("User with email: " + loginUser.getEmail() + " not found."));
        if (!passwordEncoder.matches(loginUser.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid password");
        }

        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        String token = jwtUtil.generateToken(user.getEmail());
        return new LoginResponse(token, loginUser.getEmail());
    }

    public UserResponse editUser(Long id, User updatedUser){
        User user = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User not found"));
        user.setFirstName(updatedUser.getFirstName() == null ? user.getFirstName() : updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName()== null ? user.getLastName() : updatedUser.getLastName());
        user.setEmail(updatedUser.getEmail() == null ? user.getEmail() : updatedUser.getEmail());
        user.setPassword(updatedUser.getPassword() == null ? user.getPassword() : updatedUser.getPassword());
        user.setPhoneNumber(updatedUser.getPhoneNumber() == null ? user.getPhoneNumber() : updatedUser.getPhoneNumber());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return userMapper.userToUserResponse(user);
    }

    public void deleteUser(Long id){
        if(!userRepository.existsById(id)){
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }


}

