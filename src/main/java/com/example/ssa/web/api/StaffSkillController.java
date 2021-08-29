package com.example.ssa.web.api;

import com.example.ssa.entity.skill.StaffSkill;
import com.example.ssa.service.StaffSkillService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.ssa.constants.HttpMapping.STAFF_SKILL_MAPPING;

@RequestMapping(STAFF_SKILL_MAPPING)
@RestController
public class StaffSkillController {
    private final StaffSkillService staffSkillService;

    public StaffSkillController(StaffSkillService staffSkillService) {
        this.staffSkillService = staffSkillService;
    }

    @GetMapping("/")
    public List<StaffSkill> findAll() {
        return staffSkillService.findAllStaffSkills();
    }

    @GetMapping("/{id}")
    public StaffSkill findById(@PathVariable("id") long id) throws RuntimeException {
        return staffSkillService.findStaffSkillById(id);
    }

    @GetMapping("{id}/sid/{sid}")
    public StaffSkill findBySkillIdAndStaffId(@PathVariable("id") long id, @PathVariable("sid") long sid) {
        return staffSkillService.findStaffSkillBySkillIdAndStaffId(id, sid);
    }

    @GetMapping("/sid/{id}")
    public List<StaffSkill> findAllByStaffId(@PathVariable("id") long id) {
        return staffSkillService.findAllStaffSkillsByStaffId(id);
    }

    @PostMapping("/assign")
    public StaffSkill assignSkill(@RequestBody StaffSkill skill) {
        return staffSkillService.assignStaffSkill(skill);
    }

    @PutMapping("/update")
    public StaffSkill update(@RequestBody StaffSkill skill) {
        return staffSkillService.updateStaffSkill(skill);
    }
}
