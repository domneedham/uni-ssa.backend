package com.example.ssa.web.api;

import com.example.ssa.entity.skill.Category;
import com.example.ssa.entity.skill.Skill;
import com.example.ssa.exceptions.requests.bad.CategoryDoesNotExistException;
import com.example.ssa.repository.CategoryRepository;
import com.example.ssa.repository.SkillRepository;
import com.example.ssa.repository.StaffSkillRepository;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestMapping("/api/category")
@RestController
public class CategoryController {
    private final CategoryRepository categoryRepository;
    private final SkillRepository skillRepository;
    private final StaffSkillRepository staffSkillRepository;

    public CategoryController(
            CategoryRepository categoryRepository,
            SkillRepository skillRepository,
            StaffSkillRepository staffSkillRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.skillRepository = skillRepository;
        this.staffSkillRepository = staffSkillRepository;
    }

    @GetMapping("/")
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Category> findById(@PathVariable(value = "id") Long id) {
        Optional<Category> category = categoryRepository.findById(id);

        if (category.isEmpty()) {
            throw new CategoryDoesNotExistException("Category not found with that id");
        }

        return category;
    }

    @PostMapping("/create")
    public Category create(@RequestBody Category category) {
        return categoryRepository.save(category);
    }

    @PutMapping("/update")
    public Category update(@RequestBody Category category) {
        Optional<Category> categoryToUpdate =  categoryRepository.findById(category.getId());

        if (categoryToUpdate.isEmpty()) {
            throw new CategoryDoesNotExistException("Category not found with that id");
        }

        return categoryRepository.save(category);
    }

    @DeleteMapping("/delete/{id}")
    @Transactional
    public void delete(@PathVariable(value = "id") Long id) {
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
