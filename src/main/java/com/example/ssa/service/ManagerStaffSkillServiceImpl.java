package com.example.ssa.service;

import com.example.ssa.entity.skill.ManagerStaffSkill;
import com.example.ssa.exceptions.requests.bad.ManagerStaffSkillDoesNotExistException;
import com.example.ssa.repository.ManagerStaffSkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManagerStaffSkillServiceImpl implements ManagerStaffSkillService {
    private final ManagerStaffSkillRepository managerStaffSkillRepository;

    public ManagerStaffSkillServiceImpl(ManagerStaffSkillRepository managerStaffSkillRepository) {
        this.managerStaffSkillRepository = managerStaffSkillRepository;
    }

    @Override
    public List<ManagerStaffSkill> findAllManagerStaffSkills() {
        return managerStaffSkillRepository.findAll();
    }

    @Override
    public Optional<ManagerStaffSkill> findManagerStaffSkillById(Long id) {
        Optional<ManagerStaffSkill> managerStaffSkill =  managerStaffSkillRepository.findById(id);

        if (managerStaffSkill.isEmpty()) {
            throw new ManagerStaffSkillDoesNotExistException("Skill not found with that id");
        }

        return managerStaffSkill;
    }
}
