package com.example.ssa.service;

import com.example.ssa.entity.user.Manager;
import com.example.ssa.exceptions.requests.bad.ManagerDoesNotExistException;
import com.example.ssa.repository.ManagerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManagerServiceImpl implements ManagerService {
    private final ManagerRepository managerRepository;

    public ManagerServiceImpl(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    @Override
    public List<Manager> findAllManagers() {
        return managerRepository.findAll();
    }

    @Override
    public Optional<Manager> findManagerById(Long id) {
        Optional<Manager> manager = managerRepository.findById(id);

        if (manager.isEmpty()) {
            throw new ManagerDoesNotExistException("Manager not found with that id");
        }

        return manager;
    }

    @Override
    public List<Manager> findManagersByName(String name) {
        return managerRepository.findAllByUserDetailsNameContainingIgnoreCase(name);
    }

    @Override
    public Manager createManager(Manager manager) {
        return managerRepository.save(manager);
    }

    @Override
    public Optional<Manager> findManagerByEmail(String email) {
        Optional<Manager> manager = managerRepository.findByUserDetailsEmail(email);

        if (manager.isEmpty()) {
            throw new ManagerDoesNotExistException("Manager not found with that email");
        }

        return manager;
    }
}
