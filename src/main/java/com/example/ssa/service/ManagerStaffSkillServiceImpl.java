package com.example.ssa.service;

import com.example.ssa.entity.skill.ManagerStaffSkill;
import com.example.ssa.exceptions.requests.bad.ManagerStaffSkillDoesNotExistException;
import com.example.ssa.repository.ManagerStaffSkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * A range of methods to handle ManagerStaffSkill CRUD operations.
 */
@Service
public class ManagerStaffSkillServiceImpl implements ManagerStaffSkillService {
    /**
     * The manager staff skill repository created by Spring.
     */
    private final ManagerStaffSkillRepository managerStaffSkillRepository;

    public ManagerStaffSkillServiceImpl(ManagerStaffSkillRepository managerStaffSkillRepository) {
        this.managerStaffSkillRepository = managerStaffSkillRepository;
    }

    /**
     * Find all manager staff skills.
     * @return A list of manager staff skills.
     */
    @Override
    public List<ManagerStaffSkill> findAllManagerStaffSkills() {
        return managerStaffSkillRepository.findAll();
    }

    /**
     * Find a manager staff skill by the id given.
     * @param id The id of the manager staff skill.
     * @return The found manager staff skill.
     * @throws ManagerStaffSkillDoesNotExistException If the skill does not exist.
     */
    @Override
    public ManagerStaffSkill findManagerStaffSkillById(Long id) throws ManagerStaffSkillDoesNotExistException {
        Optional<ManagerStaffSkill> managerStaffSkill =  managerStaffSkillRepository.findById(id);

        if (managerStaffSkill.isEmpty()) {
            throw new ManagerStaffSkillDoesNotExistException("Skill not found with that id");
        }

        return managerStaffSkill.get();
    }
}
