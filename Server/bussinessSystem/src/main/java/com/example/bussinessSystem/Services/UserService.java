package com.example.bussinessSystem.Services;

import com.example.bussinessSystem.Dto.LoginResponse;
import com.example.bussinessSystem.Dto.LoginUser;
import com.example.bussinessSystem.Dto.UserResponse;
import com.example.bussinessSystem.Exception.InvalidCredentialsException;
import com.example.bussinessSystem.Exception.ResourceNotFoundException;
import com.example.bussinessSystem.Mappers.UserMapper;
import com.example.bussinessSystem.Repositories.ProductRepository;
import com.example.bussinessSystem.Repositories.UserRepository;
import com.example.bussinessSystem.entities.Product;
import com.example.bussinessSystem.entities.User;
import com.example.bussinessSystem.security.BlacklistedTokens;
import com.example.bussinessSystem.security.JwtUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    final UserRepository userRepository;
    final JwtUtil jwtUtil;
    final PasswordEncoder passwordEncoder;
    final UserMapper userMapper;
    final BlacklistedTokens blacklistedTokens;
    final ProductRepository productRepository;

    public UserService(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, UserMapper userMapper, BlacklistedTokens blacklistedTokens, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.blacklistedTokens = blacklistedTokens;
        this.productRepository = productRepository;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public String getUsersName(Long id){
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return user.getFirstName() + " " + user.getLastName();
    }

    public List<Product> getFavouriteProducts(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"))
                .getFavourites();
    }

    public Product addToFavouriteProducts(String email, Long productId){
        User user =userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found."));

        List<Product> favourites = user.getFavourites();

        Product product = productRepository.findByIdAndDeletedFalse(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));
        favourites.add(product);

        user.setFavourites(favourites);
        userRepository.save(user);
        return product;
    }

    public Product toggleFavouriteProduct(String email, Long productId){
        User user =userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found."));

        List<Product> favourites = user.getFavourites();

        Product product = productRepository.findByIdAndDeletedFalse(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));
        if(favourites.contains(product)){
            favourites.remove(product);
        }
        else{
            favourites.add(product);
        }

        user.setFavourites(favourites);
        userRepository.save(user);
        return product;
    }



    public Product removeFromFavouriteProducts(String email, Long productId){
        User user =userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found."));


        List<Product> favourites =user.getFavourites();
        Product product = productRepository.findByIdAndDeletedFalse(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));
        favourites.remove(product);

        user.setFavourites(favourites);
        userRepository.save(user);
        return product;
    }

    public UserResponse addUser(User user){
        if(userRepository.existsByEmail(user.getEmail())){
            throw new IllegalArgumentException("This email is already registered!");
        }else if(userRepository.existsByPhoneNumber(user.getPhoneNumber())){
            throw new IllegalArgumentException("This phone is already registered!");
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

    public void logoutUser(String token){
        blacklistedTokens.invalidateToken(token);
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

