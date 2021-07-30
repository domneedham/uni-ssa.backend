package com.example.ssa.repository;

import com.example.ssa.entity.skill.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    List<Skill> findAllByNameContainingIgnoreCase(String name);

}
