package com.example.bussinessSystem.Controllers;

import com.example.bussinessSystem.Dto.OrderReq;
import com.example.bussinessSystem.Dto.OrderedItemsReq;
import com.example.bussinessSystem.Exception.ResourceNotFoundException;
import com.example.bussinessSystem.Repositories.OrderRepository;
import com.example.bussinessSystem.Repositories.ProductRepository;
import com.example.bussinessSystem.Repositories.UserRepository;
import com.example.bussinessSystem.Services.OrderService;
import com.example.bussinessSystem.entities.Order;
import com.example.bussinessSystem.entities.OrderedItem;
import com.example.bussinessSystem.entities.Product;
import com.example.bussinessSystem.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/business/orders")
@CrossOrigin(origins = "http://localhost:8000")
public class OrderController {

    final OrderService orderService;

    OrderController(OrderService orderRepo){
        this.orderService = orderRepo;
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
    public Order createOrder(@RequestBody OrderReq orderReq){
        return orderService.addOrder(orderReq);
    }

    @PutMapping("/{id}")
    public Order editOrder(@PathVariable Long id, @RequestBody OrderReq updatedOrder){
        return orderService.editOrder(id,updatedOrder);
    }

}