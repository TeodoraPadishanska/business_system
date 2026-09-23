package com.example.bussinessSystem.Controllers;

import com.example.bussinessSystem.Dto.OrderCreateRequest;
import com.example.bussinessSystem.Exception.ResourceNotFoundException;
import com.example.bussinessSystem.Services.OrderService;
import com.example.bussinessSystem.Services.UserService;
import com.example.bussinessSystem.entities.Order;
import com.example.bussinessSystem.entities.User;
import com.example.bussinessSystem.security.CustomUserDetailsService;
import org.springframework.http.ResponseEntity;
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

    OrderController(OrderService orderRepo, CustomUserDetailsService customUserDetailsService){
        this.orderService = orderRepo;
        this.customUserDetailsService = customUserDetailsService;
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders(){
        return ResponseEntity.ok(orderService.getAllOrders());
    }
    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id){
        return orderService.getOrderById(id);
    }

    @PostMapping
    public Order createOrder(@AuthenticationPrincipal UserDetails userDetails, @RequestBody OrderCreateRequest orderReq){
        User user = customUserDetailsService.loadUserByEmail(userDetails.getUsername());
        return orderService.addOrder(orderReq, user.getId());
    }

    @PutMapping("/{id}")
    public Order editOrder(@PathVariable Long id, @RequestBody OrderCreateRequest updatedOrder){
        return orderService.editOrder(id,updatedOrder);
    }

}