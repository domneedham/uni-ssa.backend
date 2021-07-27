package com.example.ssa.repository;

import com.example.ssa.entity.skill.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
