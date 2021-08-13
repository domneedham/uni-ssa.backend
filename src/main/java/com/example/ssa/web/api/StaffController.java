package com.example.ssa.web.api;

import com.example.ssa.entity.user.Staff;
import com.example.ssa.service.StaffService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RequestMapping("api/staff")
@RestController
public class StaffController {
    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping("/")
    public List<Staff> findAll() {
        return staffService.findAllStaff();
    }

    @GetMapping("/{id}")
    public Optional<Staff> findById(@PathVariable(value = "id") Long id) throws RuntimeException {
        return staffService.findStaffById(id);
    }

    @GetMapping("/search/{name}")
    public List<Staff> findByName(@PathVariable(value = "name") String name) {
        return staffService.findStaffByName(name);
    }

    @PostMapping("/create")
    public Staff create(@RequestBody Staff staff) throws RuntimeException {
        return staffService.createStaff(staff);
    }

    @PutMapping("/update")
    public Staff update(@RequestBody Staff staff) throws RuntimeException {
        return staffService.updateStaff(staff);
    }
}

