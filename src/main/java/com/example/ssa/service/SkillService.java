package com.example.ssa.service;

import com.example.ssa.entity.skill.Skill;

import java.util.List;

public interface SkillService {
    List<Skill> findAllSkills();
    Skill findSkillById(Long id);
    List<Skill> findSkillsByName(String name);
    Skill createSkill(Skill skill);
    Skill updateSkill(Skill skill);
    void deleteSkillById(Long id);
}
