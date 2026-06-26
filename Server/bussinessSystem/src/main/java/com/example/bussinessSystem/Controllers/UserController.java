package com.example.bussinessSystem.Controllers;

import com.example.bussinessSystem.Repositories.UserRepository;
import com.example.bussinessSystem.entities.User;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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
    public User addUser(@RequestBody User user){
        return userRepo.save(user);
    }


    //@RequestBody?
    @PutMapping("/login/{id}")
    public User loginUser(@RequestBody @PathVariable Long id ){
        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        if (user == null){
            return null;
        }
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }

    @PutMapping("/edit/{id}")
    public User editUser(@PathVariable Long id,@RequestBody User updatedUser){
        User user = userRepo.findById(id).orElseThrow(()-> new RuntimeException("User not found"));
        if(user == null){
            return null;
        }
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