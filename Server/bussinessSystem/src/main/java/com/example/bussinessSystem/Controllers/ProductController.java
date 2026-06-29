package com.example.bussinessSystem.Controllers;

import com.example.bussinessSystem.Dto.ProductReq;
import com.example.bussinessSystem.Repositories.CategoryRepository;
import com.example.bussinessSystem.Repositories.ProductRepository;
import com.example.bussinessSystem.entities.Category;
import com.example.bussinessSystem.entities.Product;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/business/products")
@CrossOrigin(origins = "http://localhost:63342")
public class ProductController {

    final ProductRepository productRepo;
    final CategoryRepository categoryRepo;
    ProductController(ProductRepository repo, CategoryRepository catRepo){
        productRepo = repo;
        categoryRepo = catRepo;
    }

    @GetMapping
    public List<Product> getAllProducts(){
        return productRepo.findAll();
    }
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id){
        return productRepo.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @PostMapping
    public Product createProduct(@RequestBody ProductReq request){

        Category category = categoryRepo
                .findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = new Product();

        product.setName(request.getName());
        product.setBrand(request.getBrand());
        product.setBarcode(request.getBarcode());
        product.setPrice(request.getPrice());
        product.setWeightQuantity(request.getWeightQuantity());
        product.setCategory(category);
        product.setQuantityAtStock(request.getQuantityAtStock());
        product.setUnit(request.getUnit());
        product.setImgUrl(request.getImgUrl());
        product.setIsOnSale(request.getIsOnSale());
        product.setSalePrice(request.getSalePrice());

        return productRepo.save(product);
    }

    @PutMapping("/{id}")
    public Product editProduct(@PathVariable Long id, @RequestBody Product updatedproduct){
        Product product = productRepo.findById(id).orElseThrow(() ->  new RuntimeException("Product not found"));
        product.setName(updatedproduct.getName()  == null ? product.getName() : updatedproduct.getName());
        product.setWeightQuantity(updatedproduct.getWeightQuantity() == null ? product.getWeightQuantity() : updatedproduct.getWeightQuantity());
        product.setPrice(updatedproduct.getPrice() == null ? product.getPrice() : updatedproduct.getPrice());
        product.setBrand(updatedproduct.getBrand() == null ? product.getBrand() : updatedproduct.getBrand());
        product.setCategory(updatedproduct.getCategory() == null ? product.getCategory() : updatedproduct.getCategory());
        product.setQuantityAtStock(updatedproduct.getQuantityAtStock() == null ? product.getQuantityAtStock() : updatedproduct.getQuantityAtStock());
        product.setUnit(updatedproduct.getUnit() == null ? product.getUnit() : updatedproduct.getUnit());
        product.setSalePrice(updatedproduct.getSalePrice() == null ? product.getSalePrice() : updatedproduct.getSalePrice());
        product.setIsOnSale(updatedproduct.getIsOnSale() == null ? product.getIsOnSale() : updatedproduct.getIsOnSale());
        product.setImgUrl(updatedproduct.getImgUrl() == null ? product.getImgUrl() : updatedproduct.getImgUrl());
        return productRepo.save(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProductById( @PathVariable Long id){
        if(!productRepo.existsById(id)){
            throw new RuntimeException("Product not found!");
        }
        productRepo.deleteById(id);
    }
}