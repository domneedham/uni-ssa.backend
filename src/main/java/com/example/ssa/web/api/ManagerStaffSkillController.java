package com.example.ssa.web.api;

import com.example.ssa.entity.skill.ManagerStaffSkill;
import com.example.ssa.service.ManagerStaffSkillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.ssa.constants.HttpMapping.MANAGER_STAFF_SKILL_MAPPING;

@Slf4j
@RequestMapping(MANAGER_STAFF_SKILL_MAPPING)
@RestController
public class ManagerStaffSkillController {
    private final ManagerStaffSkillService managerStaffSkillService;

    public ManagerStaffSkillController(ManagerStaffSkillService managerStaffSkillService) {
        this.managerStaffSkillService = managerStaffSkillService;
    }

    @GetMapping("/")
    public List<ManagerStaffSkill> findAll() {
        log.info(String.format("%s /", getClass().getName()));
        return managerStaffSkillService.findAllManagerStaffSkills();
    }

    @GetMapping("/{id}")
    public ManagerStaffSkill findById(@PathVariable("id") long id) {
        log.info(String.format("%s /{id}", getClass().getName()));
        return managerStaffSkillService.findManagerStaffSkillById(id);
    }
}
