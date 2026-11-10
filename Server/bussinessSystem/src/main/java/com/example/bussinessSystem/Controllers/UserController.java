package com.example.bussinessSystem.Controllers;

import com.example.bussinessSystem.Dto.LoginUser;
import com.example.bussinessSystem.Dto.UserResponse;
import com.example.bussinessSystem.Services.UserService;
import com.example.bussinessSystem.entities.Product;
import com.example.bussinessSystem.entities.User;

import com.example.bussinessSystem.security.CustomUserDetailsService;
import com.example.bussinessSystem.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RequestMapping("/business/users")
@RestController
@CrossOrigin(origins = "http://localhost:8000")
public class UserController {

//    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    final UserService userService;
    final JwtUtil jwtUtil;
    final PasswordEncoder passwordEncoder;
    final CustomUserDetailsService userDetailsService;

    public UserController(UserService userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, CustomUserDetailsService userDetailsService) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/favourite")
    public ResponseEntity<?> getFavouriteProducts(@AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();
        return ResponseEntity.ok(userService.getFavouriteProducts(email));
    }

    @PostMapping("/favourite/add/{productId}")
    public ResponseEntity<?> addToFavouriteProducts(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long productId
    ){
        String email = userDetails.getUsername();
        return ResponseEntity.ok(userService.addToFavouriteProducts(email, productId));
    }

    @PostMapping("/favourite/toggle/{productId}")
    public ResponseEntity<?> toggleFavouriteProduct(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long productId
    ){
        String email = userDetails.getUsername();
        return ResponseEntity.ok(userService.toggleFavouriteProduct(email, productId));
    }

    @PostMapping("/favourite/remove/{productId}")
    public ResponseEntity<?> removeFromFavouriteProducts(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long productId
    ){
        String email = userDetails.getUsername();
        return ResponseEntity.ok(userService.removeFromFavouriteProducts(email, productId));
    }

    @PostMapping("/register")
    public ResponseEntity<?> addUser(@Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.addUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginUser loginUser) {
        return ResponseEntity.ok(userService.loginUser(loginUser));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestHeader("Authorization") String bearerToken){
        String token = bearerToken.substring(7);
        userService.logoutUser(token);
        return ResponseEntity.ok("Logged out");
    }

    @GetMapping("/status")
    public ResponseEntity<?> checkTokenStatus(){
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe(@AuthenticationPrincipal UserDetails userDetails){

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        User user = userDetailsService.loadUserByEmail(userDetails.getUsername());

        return ResponseEntity.ok(Map.of(
                "email",user.getEmail(),
                "isAdmin",isAdmin
        ));
    }

    //TODO метод за забравена парола

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editUser(@PathVariable Long id,@Valid @RequestBody User updatedUser){
        return ResponseEntity.ok(userService.editUser(id, updatedUser));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}