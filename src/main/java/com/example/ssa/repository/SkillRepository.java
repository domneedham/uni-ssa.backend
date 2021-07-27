package com.example.ssa.repository;

import com.example.ssa.entity.skill.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Long> {
}
