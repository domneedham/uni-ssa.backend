package com.example.ssa.repository;

import com.example.ssa.entity.skill.Skill;
import com.example.ssa.entity.skill.StaffSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StaffSkillRepository extends JpaRepository<StaffSkill, Long> {
    List<StaffSkill> findByStaffDetailsId(Long id);
    Optional<StaffSkill> findBySkillIdAndStaffDetailsId(Long id, Long sid);
    void deleteAllBySkillId(Long id);
    void deleteAllBySkillIdIsIn(List<Long> ids);
}
