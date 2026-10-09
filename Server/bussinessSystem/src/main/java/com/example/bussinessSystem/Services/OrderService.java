package com.example.bussinessSystem.Services;

import com.example.bussinessSystem.Dto.OrderCreateRequest;
import com.example.bussinessSystem.Dto.OrderedItemsReq;
import com.example.bussinessSystem.Exception.ResourceNotFoundException;
import com.example.bussinessSystem.Mappers.OrderMapper;
import com.example.bussinessSystem.Repositories.CartRepository;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    final OrderRepository orderRepository;
    final ProductRepository productRepository;
    final UserRepository userRepository;
    final OrderMapper orderMapper;
    final CartRepository cartRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, UserRepository userRepository, OrderMapper orderMapper, CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderMapper = orderMapper;
        this.cartRepository = cartRepository;
    }

    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }


    public Order getOrderById(Long id){
        return orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found."));
    }

    public Order addOrder(OrderCreateRequest orderReq, Long userId){
        Order order = orderMapper.OrderReqToOrder(orderReq);

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        order.setUser(user);

        List<OrderedItem> orderedProducts = new ArrayList<>();
        var cart = cartRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("User cart not found!"));
        var cartItems = cart.getItems();
        double orderPrice = 0;
        for(var cartItem : cartItems){
            OrderedItem item = new OrderedItem();
            Product product = productRepository.findById(cartItem.getProduct().getId()).orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            item.setOrder(order);
            item.setProduct(product);

            if(product.getQuantityAtStock() < cartItem.getQuantity()){
                throw new RuntimeException("Not enough products at stock");
            }

            item.setQuantity(cartItem.getQuantity());
            product.setQuantityAtStock(product.getQuantityAtStock() - cartItem.getQuantity());

            orderedProducts.add(item);
            orderPrice += product.getPrice() * cartItem.getQuantity();

        }




        order.setOrderedProducts(orderedProducts);
        order.setOrderPrice(orderPrice);

        order.setPaymentStatus(PaymentStatus.UNPAID);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setFirstName(orderReq.getFirstName());
        order.setLastName(orderReq.getLastName());
        order.setEmail(orderReq.getEmail());
        order.setCity(orderReq.getCity());
        order.setCompany(orderReq.getCompany());
        order.setPhoneNumber(orderReq.getPhoneNumber());
        order.setCountry(orderReq.getCountry());
        order.setPostalCode(orderReq.getPostalCode());
        order.setAddress(order.getAddress());
        order.setOrderedOn_date(LocalDateTime.now());

        orderRepository.save(order);
        cart.getItems().clear();
        cartRepository.save(cart);

        return order;
    }

    public Order editOrder(Long id, OrderCreateRequest updatedOrder){
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found."));

        order.setCity(updatedOrder.getCity() == null ? order.getCity() : updatedOrder.getCity());
        order.setCountry(updatedOrder.getCountry() == null ? order.getCountry() : updatedOrder.getCountry());
        order.setCompany(updatedOrder.getCompany() == null ? order.getCompany(): updatedOrder.getCompany());
        order.setEmail(updatedOrder.getEmail() == null ? order.getEmail() : updatedOrder.getEmail());
        order.setFirstName(updatedOrder.getFirstName() == null ? order.getFirstName() : updatedOrder.getFirstName());
        order.setLastName(updatedOrder.getLastName() == null ? order.getLastName() : updatedOrder.getLastName());
        order.setPhoneNumber(updatedOrder.getPhoneNumber() == null ? order.getPhoneNumber() : updatedOrder.getPhoneNumber());
        order.setPostalCode(updatedOrder.getPostalCode() == null ? order.getPostalCode() : updatedOrder.getPostalCode());
        order.setAddress(updatedOrder.getAddress() == null ? order.getAddress() : updatedOrder.getAddress());
        return orderRepository.save(order);
    }
}
