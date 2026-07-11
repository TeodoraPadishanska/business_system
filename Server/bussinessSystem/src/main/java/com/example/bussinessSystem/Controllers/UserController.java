package com.example.bussinessSystem.Controllers;

import com.example.bussinessSystem.Repositories.UserRepository;
import com.example.bussinessSystem.entities.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@RequestMapping("/business/users")
@RestController
@CrossOrigin(origins = "http://localhost:63342")
public class UserController {

    final UserRepository userRepo;

    public UserController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping
    public List<User> getAllUsers(){
        return userRepo.findAll();
    }
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id){
        return userRepo.findById(id).orElseThrow(()-> new RuntimeException("User not found"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> addUser(@RequestBody User user){

        if (userRepo.existsByEmail(user.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            "error", "EMAIL_EXISTS",
                            "message", "Този email вече е използван."
                    ));
        }

        if (userRepo.existsByPhoneNumber(user.getPhoneNumber())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            "error", "PHONE_EXISTS",
                            "message", "Този телефонен номер вече е използван."
                    ));
        }

        User savedUser = userRepo.save(user);

        return ResponseEntity.ok(savedUser);
    }

    //?
    @PutMapping("/login/{id}")
    public User loginUser(@RequestBody @PathVariable Long id ){
        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }

    @PutMapping("/edit/{id}")
    public User editUser(@PathVariable Long id,@RequestBody User updatedUser){
        User user = userRepo.findById(id).orElseThrow(()-> new RuntimeException("User not found"));
        user.setFirstName(updatedUser.getFirstName() == null ? user.getFirstName() : updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName()== null ? user.getLastName() : updatedUser.getLastName());
        user.setEmail(updatedUser.getEmail() == null ? user.getEmail() : updatedUser.getEmail());
        user.setPassword(updatedUser.getPassword() == null ? user.getPassword() : updatedUser.getPassword());
        user.setRoleUser(updatedUser.getRoleUser() == null ? user.getRoleUser() : updatedUser.getRoleUser());
        user.setPhoneNumber(updatedUser.getPhoneNumber() == null ? user.getPhoneNumber() : updatedUser.getPhoneNumber());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepo.save(user);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@RequestBody @PathVariable Long id){
        if(!userRepo.existsById(id)){
            throw new RuntimeException("User not found");
        }
        userRepo.deleteById(id);
    }
}