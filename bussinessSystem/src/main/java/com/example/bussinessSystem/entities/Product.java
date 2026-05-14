package com.example.bussinessSystem.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Column(unique = true)
    private Long barcode;

    private Double price;
    private Long atStock;
    private int weight;

    @ManyToOne
    private Category category;

    private boolean isAvailable;

}
