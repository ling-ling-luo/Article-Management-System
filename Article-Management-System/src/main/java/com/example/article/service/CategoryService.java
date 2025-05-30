package com.example.article.service;

import com.example.article.dto.CategoryDTO;
import com.example.article.entity.Category;

import java.util.List;

public interface CategoryService {
    
    Category createCategory(CategoryDTO categoryDTO);
    
    Category updateCategory(Long id, CategoryDTO categoryDTO);
    
    void deleteCategory(Long id);
    
    Category getCategoryById(Long id);
    
    List<Category> getAllCategories();
    
    boolean existsByCategoryName(String name);
} 