package com.example.bussinessSystem.Dto;

import lombok.Data;

@Data
public class ProductReq {

    private String name;
    private String brand;
    private Long barcode;
    private Double price;
    private Double weight_quantity;
    private Long categoryId;
    private Long quantityAtStock;

}
