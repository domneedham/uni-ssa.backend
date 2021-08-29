package com.example.ssa.service;

import com.example.ssa.entity.skill.Skill;
import com.example.ssa.exceptions.requests.bad.SkillDoesNotExistException;
import com.example.ssa.repository.SkillRepository;
import com.example.ssa.repository.StaffSkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * A range of methods to handle Skill CRUD operations.
 */
@Service
public class SkillServiceImpl implements SkillService {
    /**
     * The skill repository created by Spring.
     */
    private final SkillRepository skillRepository;

    /**
     * The staff skill repository created by Spring.
     */
    private final StaffSkillRepository staffSkillRepository;

    public SkillServiceImpl(SkillRepository skillRepository, StaffSkillRepository staffSkillRepository) {
        this.skillRepository = skillRepository;
        this.staffSkillRepository = staffSkillRepository;
    }

    /**
     * Find all the skills that exist.
     * @return A list of skills.
     */
    @Override
    public List<Skill> findAllSkills() {
        return skillRepository.findAll();
    }

    /**
     * Find a skill with the given id.
     * @param id The id of the skill.
     * @return The skill found.
     * @throws SkillDoesNotExistException If the skill does not exist.
     */
    @Override
    public Skill findSkillById(Long id) throws SkillDoesNotExistException {
        Optional<Skill> skill = skillRepository.findById(id);

        if (skill.isEmpty()) {
            throw new SkillDoesNotExistException("Skill not found with that id");
        }

        return skill.get();
    }

    /**
     * Find a list of skills that match the name given.
     * @param name The name of the skill. This does not have to be a full match for the skill to be found.
     * @return A list of skills that contain the name.
     */
    @Override
    public List<Skill> findSkillsByName(String name) {
        return skillRepository.findAllByNameContainingIgnoreCase(name);
    }

    /**
     * Creates a skill.
     * @param skill The skill to create.
     * @return The created skill.
     */
    @Override
    public Skill createSkill(Skill skill) {
        return skillRepository.save(skill);
    }

    /**
     * Updates a skill if it already exists.
     * @param skill The new skill details.
     * @return The updated skill.
     * @throws SkillDoesNotExistException If the skill does not exist.
     */
    @Override
    public Skill updateSkill(Skill skill) throws SkillDoesNotExistException {
        Optional<Skill> skillToUpdate = skillRepository.findById(skill.getId());

        if (skillToUpdate.isEmpty()) {
            throw new SkillDoesNotExistException("Skill not found with that id");
        }

        return skillRepository.save(skill);
    }

    /**
     * Deletes a skill with the given id if it exists.
     * Also deletes all staff skills that contain the skill.
     * @param id The id of the skill to delete.
     * @throws SkillDoesNotExistException If the skill does not exist.
     */
    @Override
    public void deleteSkillById(Long id) throws SkillDoesNotExistException {
        Optional<Skill> skillToDelete = skillRepository.findById(id);

        if (skillToDelete.isEmpty()) {
            throw new SkillDoesNotExistException("Skill not found with that id");
        }

        staffSkillRepository.deleteAllBySkillId(id);
        skillRepository.deleteById(id);
    }
}
