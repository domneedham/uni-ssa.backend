package com.example.ssa.service;

import com.example.ssa.entity.skill.ManagerStaffSkill;

import java.util.List;
import java.util.Optional;

public interface ManagerStaffSkillService {
    List<ManagerStaffSkill> findAllManagerStaffSkills();
    Optional<ManagerStaffSkill> findManagerStaffSkillById(Long id);

}
