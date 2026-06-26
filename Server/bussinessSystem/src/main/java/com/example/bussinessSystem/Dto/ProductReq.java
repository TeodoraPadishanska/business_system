package com.example.bussinessSystem.Dto;

import lombok.Data;

@Data
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
    private Double salePrise;
    private String imgUrl;
}
