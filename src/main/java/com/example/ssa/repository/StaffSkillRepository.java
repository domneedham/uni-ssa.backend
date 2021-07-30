package com.example.ssa.repository;

import com.example.ssa.entity.skill.StaffSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StaffSkillRepository extends JpaRepository<StaffSkill, Long> {
    List<StaffSkill> findByStaffDetailsId(long id);
}
