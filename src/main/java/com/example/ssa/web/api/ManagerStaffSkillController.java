package com.example.ssa.web.api;

import com.example.ssa.entity.skill.ManagerStaffSkill;
import com.example.ssa.entity.user.AppUser;
import com.example.ssa.repository.ManagerStaffSkillRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestMapping("/api/skill/manager")
@RestController
public class ManagerStaffSkillController {
    private final ManagerStaffSkillRepository skillRepository;

    public ManagerStaffSkillController(ManagerStaffSkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @GetMapping("/")
    public List<ManagerStaffSkill> findAll() {
        return skillRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<ManagerStaffSkill> findById(@PathVariable("id") long id) {
        return skillRepository.findById(id);
    }

    @GetMapping("/users/{id}")
    public List<AppUser> findByStaffDetailsId(@PathVariable("id") long id) {
        List<ManagerStaffSkill> list = skillRepository.findAllBySkillId(id);
        List<AppUser> userList = new ArrayList<>();
        for (ManagerStaffSkill skill : list) {
            userList.add(skill.getStaffDetails());
        }
        return userList;
    }
}
