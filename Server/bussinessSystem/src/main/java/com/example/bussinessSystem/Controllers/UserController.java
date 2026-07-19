package com.example.bussinessSystem.Controllers;

import com.example.bussinessSystem.Dto.LoginUser;
import com.example.bussinessSystem.Repositories.UserRepository;
import com.example.bussinessSystem.entities.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@RequestMapping("/business/users")
@RestController
@CrossOrigin(origins = "http://localhost:63342")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
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
        try{
            if (userRepo.existsByEmail(user.getEmail())) {
                logger.warn("Email already exists: {}", user.getEmail());
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(Map.of(
                                "error", "EMAIL_EXISTS",
                                "message", "Този email вече е регистриран!"

                        ));
                //throw new Exception("nsadandasdn");
            }
            if (userRepo.existsByPhoneNumber(user.getPhoneNumber())) {
                logger.warn("Phone already exists: {}", user.getPhoneNumber());
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(Map.of(
                                "error", "PHONE_EXISTS",
                                "message", "Този телефонен номер вече е регистриран!"
                        ));
            }
            logger.info("Successfully added user: {}", user.getId());
            User savedUser = userRepo.save(user);
            return ResponseEntity.ok(savedUser);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    //?
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginUser loginUser){
        User user = userRepo.findByEmail(loginUser.getEmail()).orElse(null);

//        System.out.println(loginUser.getEmail());
//        System.out.println(loginUser.getPassword());
//
//        System.out.println(user);

        if(user == null){
            logger.warn("Email {} not found.", loginUser.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "message", "Не е намерен потребител с този имеейл.",
                            "error", "INVALID_EMAIL"
            ));
        }
        if(!user.getPassword().equals(loginUser.getPassword())){
            logger.warn("Wrong password for {}", user.getId());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "error", "INVALID_PASSWORD",
                            "message", "Грешна парола."
                    ));
        }

        user.setUpdatedAt(LocalDateTime.now());
        userRepo.save(user);

        logger.info("User {} logged in.", user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(
                Map.of(
                        "message", "Входът е успешен!",
                        "id", user.getId(),
                        "name", user.getFirstName()
                )
        );
//        return ResponseEntity.ok(Map.of(
//                "message", "Входът е успешен!",
//                "id", user.getId(),
//                "name", user.getFirstName()
//        ));
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