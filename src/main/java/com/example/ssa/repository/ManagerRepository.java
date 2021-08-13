package com.example.ssa.repository;

import com.example.ssa.entity.user.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
    List<Manager> findAllByUserDetailsNameContainingIgnoreCase(String name);

    Optional<Manager> findByUserDetailsEmail(String email);
}
