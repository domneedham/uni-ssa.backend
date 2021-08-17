package com.example.ssa.web.api;

import com.example.ssa.entity.skill.Category;
import com.example.ssa.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static com.example.ssa.constants.HttpMapping.CATEGORY_MAPPING;

@RequestMapping(CATEGORY_MAPPING)
@RestController
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public List<Category> findAll() {
        return categoryService.findAllCategories();
    }

    @GetMapping("/{id}")
    public Optional<Category> findById(@PathVariable(value = "id") Long id) {
        return categoryService.findCategoryById(id);
    }

    @PostMapping("/create")
    public Category create(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }

    @PutMapping("/update")
    public Category update(@RequestBody Category category) {
        return categoryService.updateCategory(category);
    }

    @DeleteMapping("/delete/{id}")
    @Transactional
    public void delete(@PathVariable(value = "id") Long id) {
        categoryService.deleteCategoryById(id);
    }
}
