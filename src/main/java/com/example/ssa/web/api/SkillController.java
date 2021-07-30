package com.example.ssa.web.api;

import com.example.ssa.entity.skill.Skill;
import com.example.ssa.repository.SkillRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/skill")
@RestController
public class SkillController {
    private final SkillRepository skillRepository;

    public SkillController(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @GetMapping("/")
    public List<Skill> findAll() {
        return skillRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Skill> findById(@PathVariable("id") long id) {
        return skillRepository.findById(id);
    }

    @PostMapping("/create")
    public Skill create(@RequestBody Skill skill) {
        return skillRepository.save(skill);
    }
}
