package com.example.ssa.web.api;

import com.example.ssa.entity.user.Manager;
import com.example.ssa.service.ManagerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.example.ssa.constants.HttpMapping.MANAGER_MAPPING;

@RequestMapping(MANAGER_MAPPING)
@RestController
public class ManagerController {
    private final ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @GetMapping("/")
    public List<Manager> findAll() {
        return managerService.findAllManagers();
    }

    @GetMapping("/{id}")
    public Manager findById(@PathVariable(value = "id") Long id) {
        return managerService.findManagerById(id);
    }

    @GetMapping("/search/{name}")
    public List<Manager> findByName(@PathVariable(value = "name") String name) {
        return managerService.findManagersByName(name);
    }

    @GetMapping("/email/{email}")
    public Manager findByEmail(@PathVariable(value = "email") String email) {
        return managerService.findManagerByEmail(email);
    }
}
