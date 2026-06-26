package com.example.bussinessSystem.Controllers;

import com.example.bussinessSystem.Repositories.CategoryRepository;
import com.example.bussinessSystem.entities.Category;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/business/categories")
@CrossOrigin(origins = "http://localhost:63342")
public class CategoryController {

    final CategoryRepository categoryRepo;

    CategoryController(CategoryRepository categoryRepo){
        this.categoryRepo = categoryRepo;
    }

    @GetMapping
    public List<Category> getAllCategories(){
        return categoryRepo.findAll();
    }

    @PostMapping
    public Category createCategory(@RequestBody Category category){
        return categoryRepo.save(category);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id){
        if(!categoryRepo.existsById(id)){
            throw new RuntimeException("Category not found!");
        }
        categoryRepo.deleteById(id);
    }
}