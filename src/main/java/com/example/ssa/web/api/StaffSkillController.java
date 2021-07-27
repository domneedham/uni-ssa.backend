package com.example.ssa.web.api;

import com.example.ssa.entity.skill.StaffSkill;
import com.example.ssa.repository.StaffSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/skill/staff")
@RestController
public class StaffSkillController {
    @Autowired
    private StaffSkillRepository skillRepository;

    @GetMapping("/")
    public List<StaffSkill> findAll() {
        return skillRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<StaffSkill> findById(@PathVariable("id") long id) {
        return skillRepository.findById(id);
    }

    @GetMapping("/sid/{id}")
    public List<StaffSkill> findByStaffId(@PathVariable("id") long id) {
        return skillRepository.findByStaffId(id);
    }
}
