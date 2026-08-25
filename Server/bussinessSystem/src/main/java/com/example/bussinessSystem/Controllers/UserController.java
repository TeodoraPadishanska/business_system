package com.example.bussinessSystem.Controllers;

import com.example.bussinessSystem.Dto.LoginUser;
import com.example.bussinessSystem.Dto.UserResponse;
import com.example.bussinessSystem.Services.UserService;
import com.example.bussinessSystem.entities.User;

import com.example.bussinessSystem.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/business/users")
@RestController
@CrossOrigin(origins = "http://localhost:8000")
public class UserController {

//    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    final UserService userService;
    final JwtUtil jwtUtil;
    final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping("/register")
    public ResponseEntity<?> addUser(@Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.addUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginUser loginUser) {
        return ResponseEntity.ok(userService.loginUser(loginUser));
    }

    //TODO метод за забравена парола

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editUser(@PathVariable Long id,@Valid @RequestBody User updatedUser){
        return ResponseEntity.ok(userService.editUser(id, updatedUser));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}