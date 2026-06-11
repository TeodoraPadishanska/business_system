package com.example.bussinessSystem.Dto;

import lombok.Data;

@Data
public class OrderedItemsReq {
    private Long productId;
    private int quantity;
}
