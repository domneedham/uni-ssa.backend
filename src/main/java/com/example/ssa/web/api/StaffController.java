package com.example.ssa.web.api;

import com.example.ssa.entity.user.Staff;
import com.example.ssa.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("api/staff")
@RestController
public class StaffController {
    @Autowired
    private StaffRepository staffRepository;

    @GetMapping("/")
    public List<Staff> findAll() {
        return staffRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Staff> findById(@PathVariable(value = "id") Long id) {
        return staffRepository.findById(id);
    }

    @PostMapping("/create")
    public Staff createNewStaff(@RequestBody Staff staff) {
        System.out.println(staff.toString());
        return staffRepository.save(staff);
    }
}
