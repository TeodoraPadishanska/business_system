package com.example.bussinessSystem.Controllers;

import com.example.bussinessSystem.Repositories.UserRepository;
import com.example.bussinessSystem.entities.User;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RequestMapping("/users")
@RestController
public class UserController {

    final UserRepository userRepo;

    public UserController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping
    public UserRepository getAllUsers(){
        return userRepo;
    }
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id){
        return userRepo.findById(id).orElse(null);
    }

    @PostMapping("/register")
    public User addUser(@RequestBody User user){
        return userRepo.save(user);
    }

    @PutMapping("/login/{id}")
    public User loginUser(@RequestBody @PathVariable Long id ){
        User user = userRepo.findById(id).orElse(null);
        if (user == null){
            return null;
        }
        user.setLastLogin(LocalDateTime.now());
        return user;
    }

    @PutMapping("/edit/{id}")
    public User editUser(@PathVariable Long id,@RequestBody User updatedUser){
        User user = userRepo.findById(id).orElse(null);
        if(user == null){
            return null;
        }
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(updatedUser.getPassword());
        user.setRoleUser(updatedUser.getRoleUser());
        user.setPhoneNumber(updatedUser.getPhoneNumber());
        user.setLastLogin(LocalDateTime.now());
        return userRepo.save(user);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@RequestBody@PathVariable Long id){
        if(!userRepo.existsById(id)){
            throw new RuntimeException("User not found");
        }
        userRepo.deleteById(id);
    }
}
