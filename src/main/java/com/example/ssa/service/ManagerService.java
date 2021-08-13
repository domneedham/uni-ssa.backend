package com.example.ssa.service;

import com.example.ssa.entity.user.Manager;

import java.util.List;
import java.util.Optional;

public interface ManagerService {
    List<Manager> findAllManagers();
    Optional<Manager> findManagerById(Long id);
    List<Manager> findManagersByName(String name);
}
