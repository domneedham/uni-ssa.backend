package com.example.ssa.web.api;

import com.example.ssa.entity.skill.StaffSkill;
import com.example.ssa.service.StaffSkillService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/skill/staff")
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
    public Optional<StaffSkill> findById(@PathVariable("id") long id) throws RuntimeException {
        return staffSkillService.findStaffSkillById(id);
    }

    @GetMapping("{id}/sid/{sid}")
    public Optional<StaffSkill> findBySkillIdAndStaffId(@PathVariable("id") long id, @PathVariable("sid") long sid) {
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
