package com.example.bussinessSystem.Services;

import com.example.bussinessSystem.Dto.OrderReq;
import com.example.bussinessSystem.Dto.OrderedItemsReq;
import com.example.bussinessSystem.Exception.ResourceNotFoundException;
import com.example.bussinessSystem.Mappers.OrderMapper;
import com.example.bussinessSystem.Repositories.OrderRepository;
import com.example.bussinessSystem.Repositories.ProductRepository;
import com.example.bussinessSystem.Repositories.UserRepository;
import com.example.bussinessSystem.entities.Order;
import com.example.bussinessSystem.entities.OrderedItem;
import com.example.bussinessSystem.entities.Product;
import com.example.bussinessSystem.entities.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    final OrderRepository orderRepository;
    final ProductRepository productRepository;
    final UserRepository userRepository;
    final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, UserRepository userRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderMapper = orderMapper;
    }

    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id){
        return orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found."));
    }

    public Order addOrder(OrderReq orderReq){
        Order order = orderMapper.OrderReqToOrder(orderReq);
        User user = userRepository.findById(orderReq.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        order.setUser(user);

        List<OrderedItem> orderedProducts = new ArrayList<>();

        for(OrderedItemsReq itemReq : orderReq.getItems()){
            OrderedItem item = new OrderedItem();
            Product product = productRepository.findById(itemReq.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Product not found"));

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
        return order;
    }

    public Order editOrder(Long id, OrderReq updatedOrder){
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found."));
        order.setOrderStatus(updatedOrder.getOrderStatus() == null ? order.getOrderStatus() : updatedOrder.getOrderStatus());
        order.setAddress(updatedOrder.getAddress() == null ? order.getAddress() : updatedOrder.getAddress());
        return orderRepository.save(order);
    }
}
