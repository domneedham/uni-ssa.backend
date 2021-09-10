package com.example.ssa.web.api;

import com.example.ssa.entity.user.Manager;
import com.example.ssa.service.ManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.ssa.constants.HttpMapping.MANAGER_MAPPING;

@Slf4j
@RequestMapping(MANAGER_MAPPING)
@RestController
public class ManagerController {
    private final ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @GetMapping("/")
    public List<Manager> findAll() {
        log.info(String.format("%s /", getClass().getName()));
        return managerService.findAllManagers();
    }

    @GetMapping("/{id}")
    public Manager findById(@PathVariable(value = "id") Long id) {
        log.info(String.format("%s /{id}", getClass().getName()));
        return managerService.findManagerById(id);
    }

    @GetMapping("/search/{name}")
    public List<Manager> findByName(@PathVariable(value = "name") String name) {
        log.info(String.format("%s /search/{name}", getClass().getName()));
        return managerService.findManagersByName(name);
    }

    @GetMapping("/email/{email}")
    public Manager findByEmail(@PathVariable(value = "email") String email) {
        log.info(String.format("%s /email/{email}", getClass().getName()));
        return managerService.findManagerByEmail(email);
    }
}
