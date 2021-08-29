package com.example.ssa.service;

import com.example.ssa.entity.skill.ManagerStaffSkill;

import java.util.List;

public interface ManagerStaffSkillService {
    List<ManagerStaffSkill> findAllManagerStaffSkills();
    ManagerStaffSkill findManagerStaffSkillById(Long id);

}
