package com.example.bussinessSystem.Controllers;

import com.example.bussinessSystem.Repositories.CategoryRepository;
import com.example.bussinessSystem.Services.CategoryService;
import com.example.bussinessSystem.entities.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/business/categories")
@CrossOrigin(origins = "http://localhost:8000")
public class CategoryController {

    final CategoryService categoryService;

    CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody Category category){
        return ResponseEntity.ok(categoryService.saveCategory(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editCategory(@RequestBody @PathVariable Long id, Category updatedCategory){
        return ResponseEntity.ok(categoryService.editCategory(id,updatedCategory));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}