package com.example.bussinessSystem.Controllers;

import com.example.bussinessSystem.Dto.OrderReq;
import com.example.bussinessSystem.Dto.OrderedItemsReq;
import com.example.bussinessSystem.Repositories.OrderRepository;
import com.example.bussinessSystem.Repositories.ProductRepository;
import com.example.bussinessSystem.Repositories.UserRepository;
import com.example.bussinessSystem.entities.Order;
import com.example.bussinessSystem.entities.OrderedItem;
import com.example.bussinessSystem.entities.Product;
import com.example.bussinessSystem.entities.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/business/orders")
public class OrderController {

    final OrderRepository orderRepo;
    final ProductRepository productRepo;
    final UserRepository userRepository;

    OrderController(OrderRepository orderRepo, ProductRepository productRepo, UserRepository userRepository){
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;
        this.userRepository = userRepository;
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
    public Order createOrder(@RequestBody OrderReq orderReq){

        Order order = new Order();

        order.setOrderPrice(orderReq.getOrderPrice());
        order.setOrderStatus(orderReq.getOrderStatus());
        order.setAddress(orderReq.getAddress());
        order.setPaymentStatus(orderReq.getPaymentStatus());


        User user = userRepository.findById(orderReq.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        order.setUser(user);

        List<OrderedItem> orderedProducts = new ArrayList<>();

        for(OrderedItemsReq itemReq : orderReq.getItems()){
            OrderedItem item = new OrderedItem();
            Product product = productRepo.findById(itemReq.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));

            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemReq.getQuantity());

            if(product.getQuantityAtStock() < itemReq.getQuantity()){
                throw new RuntimeException("Not enough products at stock");
            }

            product.setQuantityAtStock(product.getQuantityAtStock() - itemReq.getQuantity());
            orderedProducts.add(item);

        }
        order.setOrderedProducts(orderedProducts);

        return orderRepo.save(order);

    }

    @PutMapping("/{id}")
    public Order editOrder(@PathVariable Long id, @RequestBody Order updatedOrder){
        Order order = orderRepo.findById(id).orElse(null);
        if (order == null){
            return null;
        }
        order.setOrderStatus(updatedOrder.getOrderStatus() == null ? order.getOrderStatus() : updatedOrder.getOrderStatus());
        order.setAddress(updatedOrder.getAddress() == null ? order.getAddress() : updatedOrder.getAddress());
        return orderRepo.save(order);
    }

}