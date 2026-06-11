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
        product.setDescription(request.getDescription());
        product.setBarcode(request.getBarcode());
        product.setPrice(request.getPrice());
        product.setWeight(request.getWeight());
        product.setCategory(category);
        product.setQuantityAtStock(request.getQuantityAtStock());

        return productRepo.save(product);
    }

    @PutMapping("/{id}")
    public Product editProduct(@PathVariable Long id, @RequestBody Product updatedproduct){
        Product product = productRepo.findById(id).orElseThrow(() ->  new RuntimeException("Product not found"));
//        if(product ==null){
//            return null;
//        }
        product.setName(updatedproduct.getName()  == null ? product.getName() : updatedproduct.getName());
        product.setWeight(updatedproduct.getWeight() == null ? product.getWeight() : updatedproduct.getWeight());
        product.setPrice(updatedproduct.getPrice() == null ? product.getPrice() : updatedproduct.getPrice());
        product.setDescription(updatedproduct.getDescription() == null ? product.getDescription() : updatedproduct.getDescription());
        product.setCategory(updatedproduct.getCategory() == null ? product.getCategory() : updatedproduct.getCategory());
        product.setQuantityAtStock(updatedproduct.getQuantityAtStock() == null ? product.getQuantityAtStock() : updatedproduct.getQuantityAtStock());
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