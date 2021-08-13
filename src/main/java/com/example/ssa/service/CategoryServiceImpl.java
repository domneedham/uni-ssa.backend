package com.example.ssa.service;

import com.example.ssa.entity.skill.Category;
import com.example.ssa.entity.skill.Skill;
import com.example.ssa.exceptions.requests.bad.CategoryDoesNotExistException;
import com.example.ssa.repository.CategoryRepository;
import com.example.ssa.repository.SkillRepository;
import com.example.ssa.repository.StaffSkillRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final SkillRepository skillRepository;
    private final StaffSkillRepository staffSkillRepository;

    public CategoryServiceImpl(
            CategoryRepository categoryRepository,
            SkillRepository skillRepository,
            StaffSkillRepository staffSkillRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.skillRepository = skillRepository;
        this.staffSkillRepository = staffSkillRepository;
    }

    @Override
    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> findCategoryById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);

        if (category.isEmpty()) {
            throw new CategoryDoesNotExistException("Category not found with that id");
        }

        return category;
    }

    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Category category) {
        Optional<Category> categoryToUpdate =  categoryRepository.findById(category.getId());

        if (categoryToUpdate.isEmpty()) {
            throw new CategoryDoesNotExistException("Category not found with that id");
        }

        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategoryById(Long id) {
        Optional<Category> categoryToDelete = categoryRepository.findById(id);

        if (categoryToDelete.isEmpty()) {
            throw new CategoryDoesNotExistException("Category not found with that id");
        }

        List<Skill> skills = skillRepository.findAllByCategoryId(id);
        ArrayList<Long> ids = new ArrayList<>();
        skills.forEach(skill -> ids.add(skill.getId()));

        staffSkillRepository.deleteAllBySkillIdIsIn(ids);
        skillRepository.deleteAllByCategoryId(id);
        categoryRepository.deleteById(id);
    }
}
