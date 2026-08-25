package com.example.bussinessSystem.Services;

import com.example.bussinessSystem.Exception.ResourceNotFoundException;
import com.example.bussinessSystem.Repositories.CartItemRepository;
import com.example.bussinessSystem.Repositories.CartRepository;
import com.example.bussinessSystem.Repositories.ProductRepository;
import com.example.bussinessSystem.Repositories.UserRepository;
import com.example.bussinessSystem.entities.Cart;
import com.example.bussinessSystem.entities.CartItem;
import com.example.bussinessSystem.entities.Product;
import com.example.bussinessSystem.entities.User;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
//    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartService(
            CartRepository cartRepository,
//            CartItemRepository cartItemRepository,
            UserRepository userRepository,
            ProductRepository productRepository
    ){
        this.cartRepository = cartRepository;
//        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public Cart getCart(Long userId){

        Optional<Cart> cart = cartRepository.findByUserId(userId);

        return cart.orElseGet(() -> createCart(userId));

    }

    //TODO use this method
    public Cart addItem(Long userId, Long itemId){



        Cart cart = getCart(userId);

        Product product = productRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id: " + itemId + " is not found!"));


        if(cart.getItems().stream().anyMatch(ci -> ci.getProduct().getId().equals(itemId))){
            CartItem cartItem = cart.getItems()
                    .stream()
                    .filter(ci -> ci.getProduct().getId().equals(itemId))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Product with id: " + itemId + " is not found!"));

            cartItem.setQuantity(cartItem.getQuantity()+1);
        }
        else{
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(1);

            cart.getItems().add(cartItem);
        }


        return cartRepository.save(cart);

    }


    public Cart editQuantity(Long userId, Long productId, int quantity){

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero!");
        }

        Cart cart = getCart(userId);

        Optional<CartItem> cartItem = cart.getItems()
                .stream()
                .filter(x -> x.getProduct().getId().equals(productId))
                .findFirst();

        if(cartItem.isPresent()){
            cartItem.get().setQuantity(quantity);
        }
        else{
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new  ResourceNotFoundException("Product not found!"));

            CartItem newCartItem = new CartItem();
            newCartItem.setProduct(product);
            newCartItem.setCart(cart);
            newCartItem.setQuantity(quantity);

            cart.getItems().add(newCartItem);
        }

        return cartRepository.save(cart);

    }

    public void deleteCartItem(Long userId, Long productId){
        Cart cart = getCart(userId);
        for(CartItem cartItem : cart.getItems()){
            if (cartItem.getProduct().getId().equals(productId)){
                cart.getItems().remove(cartItem);
            }
        }
        cartRepository.save(cart);
    }

    private Cart createCart(Long userId) {
        Cart cart = new Cart();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id: " + userId + " is not found!"));
        cart.setUser(user);
        return cartRepository.save(cart);
    }


}
