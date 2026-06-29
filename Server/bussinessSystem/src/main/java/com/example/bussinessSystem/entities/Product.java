package com.example.bussinessSystem.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String brand;

    @Column(unique = true)
    private String barcode;

    private Double price;
    private Double weightQuantity;
    private String unit;

    @ManyToOne
    private Category category;

//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isAvailable;

    private Long quantityAtStock;
    private Boolean isOnSale;
    private Double salePrice;
    private String imgUrl;


    public void setQuantityAtStock(Long quantityAtStock){
        this.quantityAtStock = quantityAtStock;
        this.isAvailable = quantityAtStock > 0;
    }

    @PrePersist
    public void prePersist() {
        if (isAvailable == null) {
            isAvailable = quantityAtStock != null && quantityAtStock > 0;
        }
        if (isOnSale == null) {
            isOnSale = false;
        }
    }
}
