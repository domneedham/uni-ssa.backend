package com.example.ssa.web.api;

import com.example.ssa.entity.skill.ManagerStaffSkill;
import com.example.ssa.entity.skill.StaffSkill;
import com.example.ssa.entity.user.AppUser;
import com.example.ssa.repository.ManagerStaffSkillRepository;
import com.example.ssa.repository.StaffSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestMapping("/api/skill/manager")
@RestController
public class ManagerStaffSkillController {
    @Autowired
    private ManagerStaffSkillRepository skillRepository;

    @GetMapping("/")
    public List<ManagerStaffSkill> findAll() {
        return skillRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<ManagerStaffSkill> findById(@PathVariable("id") long id) {
        return skillRepository.findById(id);
    }

    @GetMapping("/skill/{id}")
    public List<AppUser> findAllUsersBySkillId(@PathVariable("id") long id) {
        List<ManagerStaffSkill> list = skillRepository.findAllBySkillId(id);
        List<AppUser> userList = new ArrayList<>();
        for (ManagerStaffSkill skill : list) {
            userList.add(skill.getStaffDetails());
        }
        return userList;
    }
}
