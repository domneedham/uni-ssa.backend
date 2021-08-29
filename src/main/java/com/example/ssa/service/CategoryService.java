package com.example.ssa.service;

import com.example.ssa.entity.skill.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAllCategories();
    Category findCategoryById(Long id);
    Category createCategory(Category category);
    Category updateCategory(Category category);
    void deleteCategoryById(Long id);
}
