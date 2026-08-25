package com.example.bussinessSystem.Mappers;

import com.example.bussinessSystem.Dto.OrderReq;
import com.example.bussinessSystem.entities.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public Order OrderReqToOrder(OrderReq orderReq){
        Order order = new Order();
        order.setOrderPrice(orderReq.getOrderPrice());
        order.setOrderStatus(orderReq.getOrderStatus());
        order.setAddress(orderReq.getAddress());
        order.setPaymentStatus(orderReq.getPaymentStatus());
        return order;
    }
}
