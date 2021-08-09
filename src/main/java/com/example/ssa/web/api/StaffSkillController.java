package com.example.ssa.web.api;

import com.example.ssa.entity.skill.StaffSkill;
import com.example.ssa.exceptions.requests.bad.StaffSkillDoesNotExistException;
import com.example.ssa.repository.StaffSkillRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/skill/staff")
@RestController
public class StaffSkillController {
    private final StaffSkillRepository skillRepository;

    public StaffSkillController(StaffSkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @GetMapping("/")
    public List<StaffSkill> findAll() {
        return skillRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<StaffSkill> findById(@PathVariable("id") long id) throws RuntimeException {
        Optional<StaffSkill> skill = skillRepository.findById(id);

        if (skill.isEmpty()) {
            throw new StaffSkillDoesNotExistException("Staff skill not found with that id");
        }

        return skill;
    }

    @GetMapping("{id}/sid/{sid}")
    public Optional<StaffSkill> findBySkillIdAndStaffId(@PathVariable("id") long id, @PathVariable("sid") long sid) {
        Optional<StaffSkill> skill = skillRepository.findBySkillIdAndStaffDetailsId(id, sid);

        if (skill.isEmpty()) {
            throw new StaffSkillDoesNotExistException("Staff skill not found with that id");
        }

        return skill;                                                                                                                                                                                                                                                
    }

    @GetMapping("/sid/{id}")
    public List<StaffSkill> findAllByStaffId(@PathVariable("id") long id) {
        return skillRepository.findByStaffDetailsId(id);
    }

    @PostMapping("/assign")
    public StaffSkill assignSkill(@RequestBody StaffSkill skill) {
        return skillRepository.save(skill);
    }

    @PutMapping("/update")
    public StaffSkill update(@RequestBody StaffSkill skill) throws Exception {
        Optional<StaffSkill> skillToUpdate = this.findBySkillIdAndStaffId(skill.getSkill().getId(), skill.getStaffDetails().getId());

        if (skillToUpdate.isEmpty()) {
            throw new Exception("Not found");
        }

        skillToUpdate.get().setRating(skill.getRating());
        skillToUpdate.get().setExpires(skill.getExpires());

        return skillRepository.save(skillToUpdate.get());
    }
}
