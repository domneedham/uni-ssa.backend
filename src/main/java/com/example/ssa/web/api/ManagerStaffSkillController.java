package com.example.ssa.web.api;

import com.example.ssa.entity.skill.ManagerStaffSkill;
import com.example.ssa.exceptions.requests.bad.ManagerStaffSkillDoesNotExistException;
import com.example.ssa.repository.ManagerStaffSkillRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/skill/manager")
@RestController
public class ManagerStaffSkillController {
    private final ManagerStaffSkillRepository skillRepository;

    public ManagerStaffSkillController(ManagerStaffSkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @GetMapping("/")
    public List<ManagerStaffSkill> findAll() {
        return skillRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<ManagerStaffSkill> findById(@PathVariable("id") long id) {
        Optional<ManagerStaffSkill> managerStaffSkill =  skillRepository.findById(id);

        if (managerStaffSkill.isEmpty()) {
            throw new ManagerStaffSkillDoesNotExistException("Skill not found with that id");
        }

        return managerStaffSkill;
    }
}
