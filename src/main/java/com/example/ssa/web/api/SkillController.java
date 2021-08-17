package com.example.ssa.web.api;

import com.example.ssa.entity.skill.Skill;
import com.example.ssa.service.SkillService;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static com.example.ssa.constants.HttpMapping.SKILL_MAPPING;

@RequestMapping(SKILL_MAPPING)
@RestController
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping("/")
    public List<Skill> findAll() {
        return skillService.findAllSkills();
    }

    @GetMapping("/{id}")
    public Optional<Skill> findById(@PathVariable("id") long id) throws RuntimeException {
        return skillService.findSkillById(id);
    }

    @GetMapping("/search/{name}")
    public List<Skill> searchByName(@PathVariable("name") String name) {
        return skillService.findSkillsByName(name);
    }

    @PostMapping("/create")
    public Skill create(@RequestBody Skill skill) {
        return skillService.createSkill(skill);
    }

    @PutMapping("/update")
    public Skill update(@RequestBody Skill skill) throws Exception {
        return skillService.updateSkill(skill);
    }

    @DeleteMapping("/delete/{id}")
    @Transactional
    public void delete(@PathVariable("id") Long id) {
        skillService.deleteSkillById(id);
    }
}


