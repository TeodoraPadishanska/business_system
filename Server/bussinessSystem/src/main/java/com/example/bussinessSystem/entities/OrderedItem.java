package com.example.bussinessSystem.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data

public class OrderedItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product product;
    private int quantity;

    private double price;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}