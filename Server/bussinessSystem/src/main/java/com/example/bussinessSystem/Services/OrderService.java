package com.example.bussinessSystem.Services;

import com.example.bussinessSystem.Dto.OrderCreateRequest;
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
import com.example.bussinessSystem.enums.OrderStatus;
import com.example.bussinessSystem.enums.PaymentStatus;
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


    // TODO: migrate to OrderResponseDto ??????
    public Order getOrderById(Long id){
        return orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found."));
    }

    public Order addOrder(OrderCreateRequest orderReq, Long userId){
        Order order = orderMapper.OrderReqToOrder(orderReq);

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        order.setUser(user);

        List<OrderedItem> orderedProducts = new ArrayList<>();
        double orderPrice = 0;
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
            orderPrice += product.getPrice() * itemReq.getQuantity();

        }
        order.setOrderedProducts(orderedProducts);
        order.setOrderPrice(orderPrice);
        order.setPaymentStatus(PaymentStatus.UNPAID);
        order.setOrderStatus(OrderStatus.PENDING);

        // TODO: order dates

        return order;
    }

    // FIXME: да се оправи с новото DTO
    public Order editOrder(Long id, OrderCreateRequest updatedOrder){
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found."));
        order.setOrderStatus(updatedOrder.getOrderStatus() == null ? order.getOrderStatus() : updatedOrder.getOrderStatus());
        order.setAddress(updatedOrder.getAddress() == null ? order.getAddress() : updatedOrder.getAddress());
        return orderRepository.save(order);
    }
}
