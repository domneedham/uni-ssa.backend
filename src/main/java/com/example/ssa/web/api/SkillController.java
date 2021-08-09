package com.example.ssa.web.api;

import com.example.ssa.entity.skill.Skill;
import com.example.ssa.exceptions.requests.bad.SkillDoesNotExistException;
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
    public Optional<Skill> findById(@PathVariable("id") long id) throws RuntimeException {
        Optional<Skill> skill = skillRepository.findById(id);

        if (skill.isEmpty()) {
            throw new SkillDoesNotExistException("Skill not found with that id");
        }

        return skill;
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
    public Skill update(@RequestBody Skill skill) throws Exception {
        Optional<Skill> skillToUpdate = skillRepository.findById(skill.getId());

        if (skillToUpdate.isEmpty()) {
            throw new SkillDoesNotExistException("Skill not found with that id");
        }

        return skillRepository.save(skill);
    }

    @DeleteMapping("/delete/{id}")
    @Transactional
    public void delete(@PathVariable("id") Long id) {
        Optional<Skill> skillToDelete = skillRepository.findById(id);

        if (skillToDelete.isEmpty()) {
            throw new SkillDoesNotExistException("Skill not found with that id");
        }

        staffSkillRepository.deleteAllBySkillId(id);
        skillRepository.deleteById(id);
    }
}


