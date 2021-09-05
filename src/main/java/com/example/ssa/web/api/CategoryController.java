package com.example.ssa.web.api;

import com.example.ssa.entity.skill.Category;
import com.example.ssa.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

import static com.example.ssa.constants.HttpMapping.CATEGORY_MAPPING;

@Slf4j
@RequestMapping(CATEGORY_MAPPING)
@RestController
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public List<Category> findAll() {
        log.info(String.format("%s /", getClass().getName()));
        return categoryService.findAllCategories();
    }

    @GetMapping("/{id}")
    public Category findById(@PathVariable(value = "id") Long id) {
        log.info(String.format("%s /{id}", getClass().getName()));
        return categoryService.findCategoryById(id);
    }

    @PostMapping("/create")
    public Category create(@RequestBody Category category) {
        log.info(String.format("%s /create", getClass().getName()));
        return categoryService.createCategory(category);
    }

    @PutMapping("/update")
    public Category update(@RequestBody Category category) {
        log.info(String.format("%s /update", getClass().getName()));
        return categoryService.updateCategory(category);
    }

    @DeleteMapping("/delete/{id}")
    @Transactional
    public void delete(@PathVariable(value = "id") Long id) {
        log.info(String.format("%s /delete/{id}", getClass().getName()));
        categoryService.deleteCategoryById(id);
    }
}
