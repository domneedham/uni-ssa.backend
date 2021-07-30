package com.example.ssa.web.api;

import com.example.ssa.entity.user.Manager;
import com.example.ssa.repository.ManagerRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/manager")
@RestController
public class ManagerController {
    private final ManagerRepository managerRepository;

    public ManagerController(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    @GetMapping("/")
    public List<Manager> findAll() {
        return managerRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Manager> findById(@PathVariable(value = "id") Long id) {
        return managerRepository.findById(id);
    }

    @GetMapping("/search/{name}")
    public List<Manager> findByName(@PathVariable(value = "name") String name) {
        return managerRepository.findAllByUserDetailsNameContainingIgnoreCase(name);
    }
}
