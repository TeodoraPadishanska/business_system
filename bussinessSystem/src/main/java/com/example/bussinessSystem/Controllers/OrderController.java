package com.example.bussinessSystem.Controllers;

import com.example.bussinessSystem.Repositories.OrderRepository;
import com.example.bussinessSystem.Repositories.ProductRepository;
import com.example.bussinessSystem.entities.Order;
import com.example.bussinessSystem.entities.Product;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/business/orders")
public class OrderController {

    final OrderRepository orderRepo;
    final ProductRepository productRepo;

    OrderController(OrderRepository orderRepo, ProductRepository productRepo){
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;
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

            Product product = order.getOrderedProducts().get(i).getProduct();

            if(product.getQuantityAtStock() > order.getOrderedProducts().get(i).getQuantity()){

                product.setQuantityAtStock(product.getQuantityAtStock() - order.getOrderedProducts().get(i).getQuantity());
                productRepo.save(product);}
            else{
                throw new RuntimeException("Not enough available products. Available: " + product.getQuantityAtStock());
            }
            order.getOrderedProducts().get(i).setOrder(order);
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