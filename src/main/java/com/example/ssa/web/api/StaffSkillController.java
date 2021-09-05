package com.example.ssa.web.api;

import com.example.ssa.entity.skill.StaffSkill;
import com.example.ssa.service.StaffSkillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.ssa.constants.HttpMapping.STAFF_SKILL_MAPPING;

@Slf4j
@RequestMapping(STAFF_SKILL_MAPPING)
@RestController
public class StaffSkillController {
    private final StaffSkillService staffSkillService;

    public StaffSkillController(StaffSkillService staffSkillService) {
        this.staffSkillService = staffSkillService;
    }

    @GetMapping("/")
    public List<StaffSkill> findAll() {
        log.info(String.format("%s /", getClass().getName()));
        return staffSkillService.findAllStaffSkills();
    }

    @GetMapping("/{id}")
    public StaffSkill findById(@PathVariable("id") long id) throws RuntimeException {
        log.info(String.format("%s /{id}", getClass().getName()));
        return staffSkillService.findStaffSkillById(id);
    }

    @GetMapping("{id}/sid/{sid}")
    public StaffSkill findBySkillIdAndStaffId(@PathVariable("id") long id, @PathVariable("sid") long sid) {
        log.info(String.format("%s /{id}/sid/{sid}", getClass().getName()));
        return staffSkillService.findStaffSkillBySkillIdAndStaffId(id, sid);
    }

    @GetMapping("/sid/{id}")
    public List<StaffSkill> findAllByStaffId(@PathVariable("id") long id) {
        log.info(String.format("%s /sid/{id}", getClass().getName()));
        return staffSkillService.findAllStaffSkillsByStaffId(id);
    }

    @PostMapping("/assign")
    public StaffSkill assignSkill(@RequestBody StaffSkill skill) {
        log.info(String.format("%s /assign", getClass().getName()));
        return staffSkillService.assignStaffSkill(skill);
    }

    @PutMapping("/update")
    public StaffSkill update(@RequestBody StaffSkill skill) {
        log.info(String.format("%s /update", getClass().getName()));
        return staffSkillService.updateStaffSkill(skill);
    }
}
