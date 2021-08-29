package com.example.ssa.service;

import com.example.ssa.entity.skill.StaffSkill;

import java.util.List;

public interface StaffSkillService {
    List<StaffSkill> findAllStaffSkills();
    StaffSkill findStaffSkillById(Long id);
    StaffSkill findStaffSkillBySkillIdAndStaffId(Long skillId, Long staffId);
    List<StaffSkill> findAllStaffSkillsByStaffId(Long staffId);
    StaffSkill assignStaffSkill(StaffSkill staffSkill);
    StaffSkill updateStaffSkill(StaffSkill staffSkill);
}
