package com.example.ssa.web.api;

import com.example.ssa.entity.user.Manager;
import com.example.ssa.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/manager")
@RestController
public class ManagerController {
    @Autowired
    private ManagerRepository managerRepository;

    @GetMapping("/")
    public List<Manager> findAll() {
        return managerRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Manager> findById(@PathVariable(value = "id") Long id) {
        return managerRepository.findById(id);
    }
}
