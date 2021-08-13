package com.example.ssa.service;

import com.example.ssa.entity.skill.StaffSkill;

import java.util.List;
import java.util.Optional;

public interface StaffSkillService {
    List<StaffSkill> findAllStaffSkills();
    Optional<StaffSkill> findStaffSkillById(Long id);
    Optional<StaffSkill> findStaffSkillBySkillIdAndStaffId(Long skillId, Long staffId);
    List<StaffSkill> findAllStaffSkillsByStaffId(Long staffId);
    StaffSkill assignStaffSkill(StaffSkill staffSkill);
    StaffSkill updateStaffSkill(StaffSkill staffSkill);
}
