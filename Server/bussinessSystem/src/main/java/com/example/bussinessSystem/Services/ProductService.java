package com.example.bussinessSystem.Services;

import com.example.bussinessSystem.Dto.ProductReq;
import com.example.bussinessSystem.Exception.ResourceNotFoundException;
import com.example.bussinessSystem.Mappers.ProductMapper;
import com.example.bussinessSystem.Repositories.CategoryRepository;
import com.example.bussinessSystem.Repositories.ProductRepository;
import com.example.bussinessSystem.entities.Category;
import com.example.bussinessSystem.entities.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    final ProductRepository productRepository;
    final CategoryRepository categoryRepository;
    final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    public List<Product> getAllUsers(){
        return productRepository.findAll();
    }

    public Product getProductById(Long id){
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    public Product createProduct(ProductReq productReq){
        Category category = categoryRepository
                .findById(productReq.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Product product = productMapper.productReqToProduct(productReq,category);

        return productRepository.save(product);
    }

    public Product editProduct(Long id, ProductReq productReq){
        Product product = productRepository.findById(id).orElseThrow(() ->  new RuntimeException("Product not found"));
        product.setName(productReq.getName()  == null ? product.getName() : productReq.getName());
        product.setWeightQuantity(productReq.getWeightQuantity() == null ? product.getWeightQuantity() : productReq.getWeightQuantity());
        product.setPrice(productReq.getPrice() == null ? product.getPrice() : productReq.getPrice());
        product.setBrand(productReq.getBrand() == null ? product.getBrand() : productReq.getBrand());
        product.setCategory(productReq.getCategoryId() == null ? product.getCategory() : categoryRepository.findById(productReq.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Category not found.")));
        product.setQuantityAtStock(productReq.getQuantityAtStock() == null ? product.getQuantityAtStock() : productReq.getQuantityAtStock());
        product.setUnit(productReq.getUnit() == null ? product.getUnit() : productReq.getUnit());
        product.setSalePrice(productReq.getSalePrice() == null ? product.getSalePrice() : productReq.getSalePrice());
        product.setIsOnSale(productReq.getIsOnSale() == null ? product.getIsOnSale() : productReq.getIsOnSale());
        product.setImgUrl(productReq.getImgUrl() == null ? product.getImgUrl() : productReq.getImgUrl());
        return productRepository.save(product);
    }

    public void deleteProduct(Long id){
        if(!productRepository.existsById(id)){
            throw new ResourceNotFoundException("Product not found.");
        }
        productRepository.deleteById(id);
    }

}
