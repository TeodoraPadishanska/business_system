package com.example.bussinessSystem.Dto;

import com.example.bussinessSystem.entities.OrderedItem;
import com.example.bussinessSystem.entities.User;
import com.example.bussinessSystem.enums.OrderStatus;
import com.example.bussinessSystem.enums.PaymentStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

import static com.example.bussinessSystem.enums.OrderStatus.*;
import static com.example.bussinessSystem.enums.PaymentStatus.*;

@Data
public class OrderReq {
    private Long userId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = PENDING;
    private Double orderPrice;
    private String address;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = UNPAID;
    private List<OrderedItemsReq> items;
}
