package com.example.ssa.service;

import com.example.ssa.entity.skill.Category;
import com.example.ssa.entity.skill.Skill;
import com.example.ssa.exceptions.requests.bad.CategoryDoesNotExistException;
import com.example.ssa.repository.CategoryRepository;
import com.example.ssa.repository.SkillRepository;
import com.example.ssa.repository.StaffSkillRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A range of methods to handle Category CRUD operations.
 */
@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
    /**
     * The category repository created by Spring.
     */
    private final CategoryRepository categoryRepository;

    /**
     * The skill repository created by Spring.
     */
    private final SkillRepository skillRepository;

    /**
     * The staff skill repository created by Spring.
     */
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

    /**
     * Finds all categories that exist.
     * @return A list of categories.
     */
    @Override
    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Find the category with the given id.
     * @param id The id of the category.
     * @return The found category.
     * @throws CategoryDoesNotExistException If the category does not exist.
     */
    @Override
    public Category findCategoryById(Long id) throws CategoryDoesNotExistException {
        Optional<Category> category = categoryRepository.findById(id);

        if (category.isEmpty()) {
            log.error(String.format("Category not found with id of %d", id));
            throw new CategoryDoesNotExistException("Category not found with that id");
        }

        return category.get();
    }

    /**
     * Creates a category.
     * @param category The category to create.
     * @return The created category.
     */
    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    /**
     * Updates a category if it already exists.
     * @param category The new category details.
     * @return The updated category.
     * @throws CategoryDoesNotExistException If the category does not exist.
     */
    @Override
    public Category updateCategory(Category category) throws CategoryDoesNotExistException {
        Optional<Category> categoryToUpdate =  categoryRepository.findById(category.getId());

        if (categoryToUpdate.isEmpty()) {
            log.error(String.format("Category not found with id of %d", category.getId()));
            throw new CategoryDoesNotExistException("Category not found with that id");
        }

        return categoryRepository.save(category);
    }

    /**
     * Deletes a category with the given id if it exists.
     * Also deletes all skills and staff skills that contain the category.
     * @param id The id of the category to delete.
     * @throws CategoryDoesNotExistException If the category does not exist.
     */
    @Override
    public void deleteCategoryById(Long id) throws CategoryDoesNotExistException {
        Optional<Category> categoryToDelete = categoryRepository.findById(id);

        if (categoryToDelete.isEmpty()) {
            log.error(String.format("Category not found with id of %d", id));
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
