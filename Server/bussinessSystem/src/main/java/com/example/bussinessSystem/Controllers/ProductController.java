package com.example.bussinessSystem.Controllers;

import com.example.bussinessSystem.Dto.ProductReq;
import com.example.bussinessSystem.Services.ProductService;
import com.example.bussinessSystem.entities.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/business/products")
@CrossOrigin(origins = "http://localhost:8000")
public class ProductController {

    final ProductService productService;
    ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(){
        return ResponseEntity.ok(productService.getAllUsers());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id){
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductReq productReq){
        return ResponseEntity.ok(productService.createProduct(productReq));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editProduct(@PathVariable Long id, @RequestBody ProductReq productReq){
        return ResponseEntity.ok(productService.editProduct(id, productReq));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductById( @PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}