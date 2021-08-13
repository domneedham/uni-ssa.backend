package com.example.ssa.service;

import com.example.ssa.entity.skill.Skill;

import java.util.List;
import java.util.Optional;

public interface SkillService {
    List<Skill> findAllSkills();
    Optional<Skill> findSkillById(Long id);
    List<Skill> findSkillsByName(String name);
    Skill createSkill(Skill skill);
    Skill updateSkill(Skill skill);
    void deleteSkillById(Long id);
}
