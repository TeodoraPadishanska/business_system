package com.example.bussinessSystem.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
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

//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isAvailable;

    private Long quantityAtStock;


    public void setQuantityAtStock(Long quantityAtStock){
        this.quantityAtStock = quantityAtStock;
        this.isAvailable = quantityAtStock > 0;
    }
}
