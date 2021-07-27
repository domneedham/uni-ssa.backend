package com.example.ssa.web.api;

import com.example.ssa.entity.skill.Skill;
import com.example.ssa.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/skill")
@RestController
public class SkillController {
    @Autowired
    private SkillRepository skillRepository;

    @GetMapping("/")
    public List<Skill> findAll() {
        return skillRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Skill> findById(@PathVariable("id") long id) {
        return skillRepository.findById(id);
    }
}
