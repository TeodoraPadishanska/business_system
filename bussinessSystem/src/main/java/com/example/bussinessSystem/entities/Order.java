package com.example.bussinessSystem.entities;

import com.example.bussinessSystem.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long orderNumber;

    @ManyToOne
    private User user;
    private OrderStatus orderStatus;
    private Double orderPrice;
    private LocalDate orderedOn;


}
