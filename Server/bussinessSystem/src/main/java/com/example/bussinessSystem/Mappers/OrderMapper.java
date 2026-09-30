package com.example.bussinessSystem.Mappers;

import com.example.bussinessSystem.Dto.OrderCreateRequest;
import com.example.bussinessSystem.entities.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public Order OrderReqToOrder(OrderCreateRequest orderReq){
        Order order = new Order();

        order.setCity(orderReq.getCity());
        order.setEmail(orderReq.getEmail());
        order.setPostalCode(orderReq.getPostalCode());
        order.setPhoneNumber(orderReq.getPhoneNumber());
        order.setFirstName(orderReq.getFirstName());
        order.setLastName(orderReq.getLastName());
        order.setCompany(orderReq.getCompany());
        order.setCountry(orderReq.getCountry());
        order.setAddress(orderReq.getAddress());

        return order;
    }
}
