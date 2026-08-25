package com.example.bussinessSystem.Dto;

import com.example.bussinessSystem.enums.OrderStatus;
import com.example.bussinessSystem.enums.PaymentStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import static com.example.bussinessSystem.enums.OrderStatus.*;
import static com.example.bussinessSystem.enums.PaymentStatus.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
