package com.example.bussinessSystem.entities;

import com.example.bussinessSystem.enums.OrderStatus;
import com.example.bussinessSystem.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data

public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private Double orderPrice;
    private LocalDate orderedOn_date = LocalDate.now();
    private String address;
    private PaymentStatus paymentStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderedItem> orderedProducts;


}
