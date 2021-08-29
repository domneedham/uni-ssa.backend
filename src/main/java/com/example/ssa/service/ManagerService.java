package com.example.ssa.service;

import com.example.ssa.entity.user.Manager;

import java.util.List;

public interface ManagerService {
    List<Manager> findAllManagers();
    Manager findManagerById(Long id);
    List<Manager> findManagersByName(String name);
    Manager createManager(Manager manager);

    Manager findManagerByEmail(String email);
}
