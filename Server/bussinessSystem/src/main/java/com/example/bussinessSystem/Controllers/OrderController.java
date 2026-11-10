package com.example.bussinessSystem.Controllers;

import com.example.bussinessSystem.Dto.OrderCreateRequest;
import com.example.bussinessSystem.Dto.OrderUpdateRequest;
import com.example.bussinessSystem.Exception.ResourceNotFoundException;
import com.example.bussinessSystem.Repositories.UserRepository;
import com.example.bussinessSystem.Services.OrderService;
import com.example.bussinessSystem.Services.UserService;
import com.example.bussinessSystem.entities.Order;
import com.example.bussinessSystem.entities.OrderedItem;
import com.example.bussinessSystem.entities.User;
import com.example.bussinessSystem.security.CustomUserDetailsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/business/orders")
@CrossOrigin(origins = "http://localhost:8000")
public class OrderController {

    final OrderService orderService;
    final CustomUserDetailsService customUserDetailsService;
    final UserRepository userRepository;

    OrderController(OrderService orderRepo, CustomUserDetailsService customUserDetailsService, UserRepository userRepository){
        this.orderService = orderRepo;
        this.customUserDetailsService = customUserDetailsService;
        this.userRepository = userRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders(){
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrdersByUser(@AuthenticationPrincipal UserDetails userDetails){
        User user = customUserDetailsService.loadUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(orderService.getAllOrdersByUser(user.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id){
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/{orderId}/items")
    public ResponseEntity<List<OrderedItem>> getOrderProducts(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long orderId){
        User user = customUserDetailsService.loadUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(orderService.getOrderItems(user,orderId));
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody OrderCreateRequest orderReq){
        User user = customUserDetailsService.loadUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(orderService.addOrder(orderReq, user.getId()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Order> editOrder(@PathVariable Long id, @Valid @RequestBody OrderUpdateRequest updatedOrder){
        return ResponseEntity.ok(orderService.editOrder(id,updatedOrder));
    }

    // FIXME: CORS error
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public ResponseEntity<?> deleteOrder(@PathVariable Long id){
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

}