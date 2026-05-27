package com.example.bussinessSystem.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
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
    private Double weight;

    @ManyToOne
    private Category category;

    private boolean isAvailable;
    private Long quantityAtStock;

    public void setAvailable() {
        if(quantityAtStock > 0){
            isAvailable = true;
        }else {
            isAvailable = false;
        }
    }
}
