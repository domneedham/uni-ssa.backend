package com.example.ssa.repository;

import com.example.ssa.entity.skill.StaffSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StaffSkillRepository extends JpaRepository<StaffSkill, Long> {
    List<StaffSkill> findByStaffDetailsId(long id);
    Optional<StaffSkill> findBySkillIdAndStaffDetailsId(long id, long sid);
}
