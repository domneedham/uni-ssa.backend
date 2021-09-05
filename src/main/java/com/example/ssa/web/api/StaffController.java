package com.example.ssa.web.api;

import com.example.ssa.entity.user.Staff;
import com.example.ssa.service.StaffService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static com.example.ssa.constants.HttpMapping.STAFF_MAPPING;

@Slf4j
@RequestMapping(STAFF_MAPPING)
@RestController
public class StaffController {
    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping("/")
    public List<Staff> findAll() {
        log.info(String.format("%s /", getClass().getName()));
        return staffService.findAllStaff();
    }

    @GetMapping("/{id}")
    public Staff findById(@PathVariable(value = "id") Long id) {
        log.info(String.format("%s /{id}", getClass().getName()));
        return staffService.findStaffById(id);
    }

    @GetMapping("/search/{name}")
    public List<Staff> findByName(@PathVariable(value = "name") String name) {
        log.info(String.format("%s /search/{name}", getClass().getName()));
        return staffService.findStaffByName(name);
    }

    @GetMapping("/email/{email}")
    public Staff findByEmail(@PathVariable(value = "email") String email) {
        log.info(String.format("%s /email/{email}", getClass().getName()));
        return staffService.findStaffByEmail(email);
    }

    @PostMapping("/create")
    public Staff create(@RequestBody Staff staff) {
        log.info(String.format("%s /create", getClass().getName()));
        return staffService.createStaff(staff);
    }

    @PutMapping("/update")
    public Staff update(@RequestBody Staff staff) {
        log.info(String.format("%s /update", getClass().getName()));
        return staffService.updateStaff(staff);
    }
}

