package com.example.bussinessSystem.Controllers;

import com.example.bussinessSystem.Repositories.OrderRepository;
import com.example.bussinessSystem.entities.Order;
import com.example.bussinessSystem.entities.Product;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/{id}")
public class OrderController {

    final OrderRepository orderRepo;

    OrderController(OrderRepository orderRepo){
        this.orderRepo = orderRepo;
    }

    @GetMapping
    public List<Order> getAllOrders(){
        return orderRepo.findAll();
    }
    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id){
        return orderRepo.findById(id).orElse(null);
    }

    @PostMapping
    public Order createOrder(@RequestBody Order order){
        for (int i = 0; i < order.getOrderedProducts().size(); i++) {
            if(order.getOrderedProducts().get(i).getProduct().getQuantityAtStock() > order.getOrderedProducts().get(i).getQuantity()){
                order.getOrderedProducts().get(i).getProduct().setQuantityAtStock(order.getOrderedProducts().get(i).getProduct().getQuantityAtStock()-order.getOrderedProducts().get(i).getQuantity());
            }else{
                throw new RuntimeException("Not enough available products. Available: " + order.getOrderedProducts().get(i).getProduct().getQuantityAtStock());
            }
        }
        return orderRepo.save(order);
    }

    @PutMapping("/{id}")
    public Order editOrder(@PathVariable Long id, @RequestBody Order updatedOrder){
        Order order = orderRepo.findById(id).orElse(null);
        if (order == null){
            return null;
        }
        order.setOrderStatus(updatedOrder.getOrderStatus());
        order.setAddress(updatedOrder.getAddress());
        return orderRepo.save(order);
    }

}
