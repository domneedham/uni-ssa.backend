package com.example.ssa.service;

import com.example.ssa.entity.skill.StaffSkill;
import com.example.ssa.exceptions.requests.bad.StaffSkillDoesNotExistException;
import com.example.ssa.repository.StaffSkillRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * A range of methods to handle Staff Skill CRUD operations.
 */
@Slf4j
@Service
public class StaffSkillServiceImpl implements StaffSkillService {
    /**
     * The staff skill repository created by Spring.
     */
    private final StaffSkillRepository staffSkillRepository;

    public StaffSkillServiceImpl(StaffSkillRepository staffSkillRepository) {
        this.staffSkillRepository = staffSkillRepository;
    }

    /**
     * FInd all staff skills that exist.
     * @return A list of staff skills.
     */
    @Override
    public List<StaffSkill> findAllStaffSkills() {
        return staffSkillRepository.findAll();
    }

    /**
     * Find the staff skill with the given id.
     * @param id The id of the staff skill.
     * @return The staff skill if found.
     * @throws StaffSkillDoesNotExistException If the staff skill does not exist.
     */
    @Override
    public StaffSkill findStaffSkillById(Long id) throws StaffSkillDoesNotExistException {
        Optional<StaffSkill> skill = staffSkillRepository.findById(id);

        if (skill.isEmpty()) {
            log.error(String.format("Staff not found with id of %d", id));
            throw new StaffSkillDoesNotExistException("Staff skill not found with that id");
        }

        return skill.get();
    }

    /**
     * Find a staff skill that has the skill id and the staff id.
     * @param skillId The id of the skill.
     * @param staffId The id of the staff member.
     * @return The found the staff skill.
     * @throws StaffSkillDoesNotExistException If the staff skill does not exist.
     */
    @Override
    public StaffSkill findStaffSkillBySkillIdAndStaffId(Long skillId, Long staffId) throws StaffSkillDoesNotExistException {
        Optional<StaffSkill> skill = staffSkillRepository.findBySkillIdAndStaffDetailsId(skillId, staffId);

        if (skill.isEmpty()) {
            log.error(String.format("Staff not found with id of %d", skillId));
            throw new StaffSkillDoesNotExistException("Staff skill not found with that id");
        }

        return skill.get();
    }

    /**
     * Finds all staff skills associated with the staff member.
     * @param staffId The id of the staff member.
     * @return A list of staff skills.
     */
    @Override
    public List<StaffSkill> findAllStaffSkillsByStaffId(Long staffId) {
        return staffSkillRepository.findByStaffDetailsId(staffId);
    }

    /**
     * Assign a skill to a staff member to create a new staff skill.
     * @param staffSkill The staff skill to save.
     * @return The created staff skill.
     */
    @Override
    public StaffSkill assignStaffSkill(StaffSkill staffSkill) {
        return staffSkillRepository.save(staffSkill);
    }

    /**
     * Update an assigned staff skill.
     * @param staffSkill The new values of the staff skill.
     * @return The updated staff skill.
     */
    @Override
    public StaffSkill updateStaffSkill(StaffSkill staffSkill) {
        StaffSkill skillToUpdate = this.findStaffSkillBySkillIdAndStaffId(staffSkill.getSkill().getId(), staffSkill.getStaffDetails().getId());

        skillToUpdate.setRating(staffSkill.getRating());
        skillToUpdate.setExpires(staffSkill.getExpires());

        return staffSkillRepository.save(skillToUpdate);
    }
}
