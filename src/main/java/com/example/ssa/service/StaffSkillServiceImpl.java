package com.example.ssa.service;

import com.example.ssa.entity.skill.StaffSkill;
import com.example.ssa.exceptions.requests.bad.StaffSkillDoesNotExistException;
import com.example.ssa.repository.StaffSkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaffSkillServiceImpl implements StaffSkillService {
    private final StaffSkillRepository staffSkillRepository;

    public StaffSkillServiceImpl(StaffSkillRepository staffSkillRepository) {
        this.staffSkillRepository = staffSkillRepository;
    }

    @Override
    public List<StaffSkill> findAllStaffSkills() {
        return staffSkillRepository.findAll();
    }

    @Override
    public Optional<StaffSkill> findStaffSkillById(Long id) {
        Optional<StaffSkill> skill = staffSkillRepository.findById(id);

        if (skill.isEmpty()) {
            throw new StaffSkillDoesNotExistException("Staff skill not found with that id");
        }

        return skill;
    }

    @Override
    public Optional<StaffSkill> findStaffSkillBySkillIdAndStaffId(Long skillId, Long staffId) {
        Optional<StaffSkill> skill = staffSkillRepository.findBySkillIdAndStaffDetailsId(skillId, staffId);

        if (skill.isEmpty()) {
            throw new StaffSkillDoesNotExistException("Staff skill not found with that id");
        }

        return skill;
    }

    @Override
    public List<StaffSkill> findAllStaffSkillsByStaffId(Long staffId) {
        return staffSkillRepository.findByStaffDetailsId(staffId);
    }

    @Override
    public StaffSkill assignStaffSkill(StaffSkill staffSkill) {
        return staffSkillRepository.save(staffSkill);
    }

    @Override
    public StaffSkill updateStaffSkill(StaffSkill staffSkill) {
        Optional<StaffSkill> skillToUpdate = this.findStaffSkillBySkillIdAndStaffId(staffSkill.getSkill().getId(), staffSkill.getStaffDetails().getId());

        if (skillToUpdate.isEmpty()) {
            throw new StaffSkillDoesNotExistException("Staff skill not found with that id");
        }

        skillToUpdate.get().setRating(staffSkill.getRating());
        skillToUpdate.get().setExpires(staffSkill.getExpires());

        return staffSkillRepository.save(skillToUpdate.get());
    }
}
