package com.example.bussinessSystem.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductReq {

    private String name;
    private String brand;
    private String barcode;
    private Double price;
    private Double weightQuantity;
    private Long categoryId;
    private Long quantityAtStock;
    private String unit;
    private Boolean isOnSale;
    private Double salePrice;
    private String imgUrl;
}
