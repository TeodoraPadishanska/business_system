package com.example.bussinessSystem.Mappers;

import com.example.bussinessSystem.Dto.ProductReq;
import com.example.bussinessSystem.entities.Category;
import com.example.bussinessSystem.entities.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public Product productReqToProduct(ProductReq productReq, Category category){
        Product product = new Product();

        product.setName(productReq.getName());
        product.setBrand(productReq.getBrand());
        product.setBarcode(productReq.getBarcode());
        product.setPrice(productReq.getPrice());
        product.setWeightQuantity(productReq.getWeightQuantity());
        product.setCategory(category);
        product.setQuantityAtStock(productReq.getQuantityAtStock());
        product.setUnit(productReq.getUnit());
        product.setImgUrl(productReq.getImgUrl());
        product.setIsOnSale(productReq.getIsOnSale());
        product.setSalePrice(productReq.getSalePrice());

        return product;
    }


}
