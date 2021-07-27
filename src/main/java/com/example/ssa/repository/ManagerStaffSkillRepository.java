package com.example.ssa.repository;

import com.example.ssa.entity.skill.ManagerStaffSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManagerStaffSkillRepository extends JpaRepository<ManagerStaffSkill, Long> {
    List<ManagerStaffSkill> findAllBySkillId(long skillId);
}
