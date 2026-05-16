package com.example.bussinessSystem.entities;

import com.example.bussinessSystem.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;
    private OrderStatus orderStatus;
    private Double orderPrice;
    private LocalDate orderedOn_date;
    private String address;

    private List<OrderedItem> orderedProducts;


    public List<OrderedItem> getOrderedProducts(){
        return orderedProducts;
    }
}
