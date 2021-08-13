package com.example.ssa.service;

import com.example.ssa.entity.skill.Skill;
import com.example.ssa.exceptions.requests.bad.SkillDoesNotExistException;
import com.example.ssa.repository.SkillRepository;
import com.example.ssa.repository.StaffSkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SkillServiceImpl implements SkillService {
    private final SkillRepository skillRepository;
    private final StaffSkillRepository staffSkillRepository;

    public SkillServiceImpl(SkillRepository skillRepository, StaffSkillRepository staffSkillRepository) {
        this.skillRepository = skillRepository;
        this.staffSkillRepository = staffSkillRepository;
    }

    @Override
    public List<Skill> findAllSkills() {
        return skillRepository.findAll();
    }

    @Override
    public Optional<Skill> findSkillById(Long id) {
        Optional<Skill> skill = skillRepository.findById(id);

        if (skill.isEmpty()) {
            throw new SkillDoesNotExistException("Skill not found with that id");
        }

        return skill;
    }

    @Override
    public List<Skill> findSkillsByName(String name) {
        return skillRepository.findAllByNameContainingIgnoreCase(name);
    }

    @Override
    public Skill createSkill(Skill skill) {
        return skillRepository.save(skill);
    }

    @Override
    public Skill updateSkill(Skill skill) {
        Optional<Skill> skillToUpdate = skillRepository.findById(skill.getId());

        if (skillToUpdate.isEmpty()) {
            throw new SkillDoesNotExistException("Skill not found with that id");
        }

        return skillRepository.save(skill);
    }

    @Override
    public void deleteSkillById(Long id) {
        Optional<Skill> skillToDelete = skillRepository.findById(id);

        if (skillToDelete.isEmpty()) {
            throw new SkillDoesNotExistException("Skill not found with that id");
        }

        staffSkillRepository.deleteAllBySkillId(id);
        skillRepository.deleteById(id);
    }
}
