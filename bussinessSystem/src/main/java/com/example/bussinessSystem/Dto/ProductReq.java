package com.example.bussinessSystem.Dto;

import lombok.Data;

@Data
public class ProductReq {

    private String name;
    private String description;
    private Long barcode;
    private Double price;
    private Double weight;
    private Long categoryId;
    private Long quantityAtStock;

    private boolean isAvailable;

    public void setAvailable() {
        if(quantityAtStock > 0){
            isAvailable = true;
        }else {
            isAvailable = false;
        }
    }
}
