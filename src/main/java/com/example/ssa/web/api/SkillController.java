package com.example.ssa.web.api;

import com.example.ssa.entity.skill.Skill;
import com.example.ssa.service.SkillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

import static com.example.ssa.constants.HttpMapping.SKILL_MAPPING;

@Slf4j
@RequestMapping(SKILL_MAPPING)
@RestController
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping("/")
    public List<Skill> findAll() {
        log.info(String.format("%s /", getClass().getName()));
        return skillService.findAllSkills();
    }

    @GetMapping("/{id}")
    public Skill findById(@PathVariable("id") long id) throws RuntimeException {
        log.info(String.format("%s /{id}", getClass().getName()));
        return skillService.findSkillById(id);
    }

    @GetMapping("/search/{name}")
    public List<Skill> searchByName(@PathVariable("name") String name) {
        log.info(String.format("%s /search/{name}", getClass().getName()));
        return skillService.findSkillsByName(name);
    }

    @PostMapping("/create")
    public Skill create(@RequestBody Skill skill) {
        log.info(String.format("%s /create", getClass().getName()));
        return skillService.createSkill(skill);
    }

    @PutMapping("/update")
    public Skill update(@RequestBody Skill skill) {
        log.info(String.format("%s /update", getClass().getName()));
        return skillService.updateSkill(skill);
    }

    @DeleteMapping("/delete/{id}")
    @Transactional
    public void delete(@PathVariable("id") Long id) {
        log.info(String.format("%s /delete/{id}", getClass().getName()));
        skillService.deleteSkillById(id);
    }
}


