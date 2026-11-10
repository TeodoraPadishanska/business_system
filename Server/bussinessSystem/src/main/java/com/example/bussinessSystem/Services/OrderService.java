package com.example.bussinessSystem.Services;

import com.example.bussinessSystem.Dto.OrderCreateRequest;
import com.example.bussinessSystem.Dto.OrderUpdateRequest;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public List<Order> getAllOrdersByUser(Long userId){
        return orderRepository.findAllByUserId(userId);
    }

    public List<OrderedItem> getOrderItems(User user,Long orderId){
        var order =  orderRepository.findById(orderId)
                .orElseThrow(()-> new ResourceNotFoundException("Order not found."));


        if(order.getUser().getId().equals(user.getId())){
            return order.getOrderedProducts();
        }
        else{
            throw new AccessDeniedException("Order does not belong to this user.");
        }
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
            Product product = productRepository.findByIdAndDeletedFalse(cartItem.getProduct().getId()).orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            item.setOrder(order);
            item.setProduct(product);
            item.setPrice(product.getPrice());

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

        // TODO: smeni go
        order.setExpectedDelivery(order.getOrderedOn_date().toLocalDate().plusDays(5));

        orderRepository.save(order);
        cart.getItems().clear();
        cartRepository.save(cart);

        return order;
    }

    public Order editOrder(Long id, OrderUpdateRequest updatedOrder) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found."));

        // Customer information
        order.setEmail(updatedOrder.getEmail());
        order.setFirstName(updatedOrder.getFirstName());
        order.setLastName(updatedOrder.getLastName());
        order.setCompany(updatedOrder.getCompany() == null ? "" : updatedOrder.getCompany());
        order.setPhoneNumber(updatedOrder.getPhoneNumber());

        // Delivery information
        order.setAddress(updatedOrder.getAddress());
        order.setCity(updatedOrder.getCity());
        order.setCountry(updatedOrder.getCountry());
        order.setPostalCode(updatedOrder.getPostalCode());
        order.setExpectedDelivery(updatedOrder.getExpectedDelivery());

        // Order information
        order.setOrderStatus(updatedOrder.getOrderStatus());
        order.setPaymentStatus(updatedOrder.getPaymentStatus());

        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
