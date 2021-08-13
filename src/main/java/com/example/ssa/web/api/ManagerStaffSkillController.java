package com.example.ssa.web.api;

import com.example.ssa.entity.skill.ManagerStaffSkill;
import com.example.ssa.service.ManagerStaffSkillService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/skill/manager")
@RestController
public class ManagerStaffSkillController {
    private final ManagerStaffSkillService managerStaffSkillService;

    public ManagerStaffSkillController(ManagerStaffSkillService managerStaffSkillService) {
        this.managerStaffSkillService = managerStaffSkillService;
    }

    @GetMapping("/")
    public List<ManagerStaffSkill> findAll() {
        return managerStaffSkillService.findAllManagerStaffSkills();
    }

    @GetMapping("/{id}")
    public Optional<ManagerStaffSkill> findById(@PathVariable("id") long id) {
        return managerStaffSkillService.findManagerStaffSkillById(id);
    }
}
