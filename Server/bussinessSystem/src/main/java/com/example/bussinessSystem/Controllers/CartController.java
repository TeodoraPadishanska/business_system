package com.example.bussinessSystem.Controllers;

import com.example.bussinessSystem.Services.CartService;
import com.example.bussinessSystem.Services.ProductService;
import com.example.bussinessSystem.entities.Cart;
import com.example.bussinessSystem.entities.CartItem;
import com.example.bussinessSystem.entities.Product;
import com.example.bussinessSystem.entities.User;
import com.example.bussinessSystem.security.CustomUserDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/business/cart")
@CrossOrigin(origins = "http://localhost:8000")
public class CartController {
    private final CartService cartService;
    private final CustomUserDetailsService userDetailsService;
    private final ProductService productService;

    public CartController(CartService cartService, CustomUserDetailsService userDetailsService, ProductService productService){
        this.cartService = cartService;
        this.userDetailsService =userDetailsService;
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<?> getCart(@AuthenticationPrincipal UserDetails userDetails){
        User user = userDetailsService.loadUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(cartService.getCart(user.getId()).getItems());
    }

    @GetMapping("/value")
    public ResponseEntity<?> getCartValue(@AuthenticationPrincipal UserDetails userDetails){
        User user = userDetailsService.loadUserByEmail(userDetails.getUsername());

        return ResponseEntity.ok(cartService.getCartValue(user.getId()));
    }

    @PostMapping("/{productId}")
    public ResponseEntity<?> addToCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long productId
    ){
        User user = userDetailsService.loadUserByEmail(userDetails.getUsername());

        return ResponseEntity.ok().body(cartService.addItem(user.getId(),productId).getItems());
    }

    @PatchMapping("/{productId}/{quantity}")
    public ResponseEntity<?> editQuantity(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long productId,
            @PathVariable int quantity
    ){
        User user = userDetailsService.loadUserByEmail(userDetails.getUsername());

        return ResponseEntity.ok().body(cartService.editQuantity(user.getId(), productId, quantity).getItems());
    }


    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteCartItem(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long productId){
        User user = userDetailsService.loadUserByEmail(userDetails.getUsername());
        cartService.deleteCartItem(user.getId(), productId);
        return ResponseEntity.noContent().build();
    }

}
