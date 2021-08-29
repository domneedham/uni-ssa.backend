package com.example.ssa.web.api;

import com.example.ssa.entity.user.Staff;
import com.example.ssa.service.StaffService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

import static com.example.ssa.constants.HttpMapping.STAFF_MAPPING;

@RequestMapping(STAFF_MAPPING)
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
    public Staff findById(@PathVariable(value = "id") Long id) {
        return staffService.findStaffById(id);
    }

    @GetMapping("/search/{name}")
    public List<Staff> findByName(@PathVariable(value = "name") String name) {
        return staffService.findStaffByName(name);
    }

    @GetMapping("/email/{email}")
    public Staff findByEmail(@PathVariable(value = "email") String email) {
        return staffService.findStaffByEmail(email);
    }

    @PostMapping("/create")
    public Staff create(@RequestBody Staff staff) {
        return staffService.createStaff(staff);
    }

    @PutMapping("/update")
    public Staff update(@RequestBody Staff staff) {
        return staffService.updateStaff(staff);
    }
}

