package com.example.ssa.web.api;

import com.example.ssa.entity.skill.Skill;
import com.example.ssa.repository.SkillRepository;
import com.example.ssa.repository.StaffSkillRepository;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RequestMapping("/api/skill")
@RestController
public class SkillController {
    private final SkillRepository skillRepository;
    private final StaffSkillRepository staffSkillRepository;

    public SkillController(SkillRepository skillRepository, StaffSkillRepository staffSkillRepository) {
        this.skillRepository = skillRepository;
        this.staffSkillRepository = staffSkillRepository;
    }

    @GetMapping("/")
    public List<Skill> findAll() {
        return skillRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Skill> findById(@PathVariable("id") long id) {
        return skillRepository.findById(id);
    }

    @GetMapping("/search/{name}")
    public List<Skill> searchByName(@PathVariable("name") String name) {
        return skillRepository.findAllByNameContainingIgnoreCase(name);
    }

    @PostMapping("/create")
    public Skill create(@RequestBody Skill skill) {
        return skillRepository.save(skill);
    }

    @PutMapping("/update")
    public Skill update(@RequestBody Skill skill) {
        return skillRepository.save(skill);
    }

    @DeleteMapping("/delete/{id}")
    @Transactional
    public void delete(@PathVariable("id") long id) {
        staffSkillRepository.deleteAllBySkillId(id);
        skillRepository.deleteById(id);
    }
}
