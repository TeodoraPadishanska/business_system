package com.example.bussinessSystem.Services;

import com.example.bussinessSystem.Exception.ResourceNotFoundException;
import com.example.bussinessSystem.Repositories.CategoryRepository;
import com.example.bussinessSystem.entities.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    public Category saveCategory(Category category){
        return categoryRepository.save(category);
    }

    public Category editCategory(Long categoryId, Category updatedCategory){
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        category.setName(updatedCategory.getName() == null ? category.getName() : updatedCategory.getName());
        category.setDescription(updatedCategory.getDescription() == null ? category.getDescription() : updatedCategory.getDescription());
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long categoryId){
        if(!categoryRepository.existsById(categoryId)){
            throw new RuntimeException("Category not found!");
        }
        categoryRepository.deleteById(categoryId);
    }


}
