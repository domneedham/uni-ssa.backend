package com.example.ssa.service;

import com.example.ssa.entity.user.Manager;
import com.example.ssa.entity.user.Staff;

import java.util.List;
import java.util.Optional;

public interface ManagerService {
    List<Manager> findAllManagers();
    Optional<Manager> findManagerById(Long id);
    List<Manager> findManagersByName(String name);
    Manager createManager(Manager manager);

    Optional<Manager> findManagerByEmail(String email);
}
